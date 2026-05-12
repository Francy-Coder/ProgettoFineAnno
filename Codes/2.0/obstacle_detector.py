import cv2
import numpy as np
import torch
from torchvision import models, transforms
from PIL import Image
import base64
import json
from flask import Flask, request, jsonify

app = Flask(__name__)

# Carica modello MobileNetV3 pre-addestrato (leggero, ottimo per mobile)
model = models.mobilenet_v3_small(pretrained=True)
model.eval()

# Classi COCO rilevanti per la sicurezza stradale dei non vedenti
CLASSI_PERICOLOSE = {
    # indice COCO: nome leggibile
    0:  "persona",
    1:  "bicicletta",
    2:  "auto",
    3:  "moto",
    9:  "semaforo",
    11: "fermata",
    56: "sedia",
    57: "divano",
    60: "tavolo",
    72: "lavori in corso",
}

preprocess = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(
        mean=[0.485, 0.456, 0.406],
        std=[0.229, 0.224, 0.225]
    )
])

@app.route('/analizza', methods=['POST'])
def analizza():
    """
    Riceve frame base64 dall'app Android.
    Restituisce JSON con ostacoli rilevati e messaggio vocale.
    """
    try:
        data = request.get_json()
        img_b64 = data.get('frame')
        img_bytes = base64.b64decode(img_b64)

        nparr = np.frombuffer(img_bytes, np.uint8)
        img_cv = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
        img_rgb = cv2.cvtColor(img_cv, cv2.COLOR_BGR2RGB)
        pil_img = Image.fromarray(img_rgb)

        tensor = preprocess(pil_img).unsqueeze(0)

        with torch.no_grad():
            output = model(tensor)

        probabilita = torch.nn.functional.softmax(output[0], dim=0)
        top5 = torch.topk(probabilita, 5)

        ostacoli = []
        for prob, idx in zip(top5.values, top5.indices):
            idx_int = idx.item()
            if idx_int in CLASSI_PERICOLOSE and prob.item() > 0.3:
                ostacoli.append({
                    "classe": CLASSI_PERICOLOSE[idx_int],
                    "confidenza": round(prob.item(), 2)
                })

        # Genera messaggio vocale
        if ostacoli:
            nomi = ", ".join([o["classe"] for o in ostacoli])
            messaggio = f"Attenzione! Rilevato: {nomi} davanti a te."
        else:
            messaggio = "Strada libera."

        return jsonify({
            "ostacoli": ostacoli,
            "messaggio_vocale": messaggio,
            "sicuro": len(ostacoli) == 0
        })

    except Exception as e:
        return jsonify({"errore": str(e)}), 500


@app.route('/semaforo', methods=['POST'])
def analizza_semaforo():
    """
    Analisi specifica per riconoscimento semafori (rosso/verde).
    """
    try:
        data = request.get_json()
        img_b64 = data.get('frame')
        img_bytes = base64.b64decode(img_b64)

        nparr = np.frombuffer(img_bytes, np.uint8)
        img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
        img_hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

        # Maschera rosso (due range per coprire H=0 e H=180)
        maschera_r1 = cv2.inRange(img_hsv,
            np.array([0, 100, 100]), np.array([10, 255, 255]))
        maschera_r2 = cv2.inRange(img_hsv,
            np.array([160, 100, 100]), np.array([180, 255, 255]))
        maschera_rosso = cv2.add(maschera_r1, maschera_r2)

        # Maschera verde
        maschera_verde = cv2.inRange(img_hsv,
            np.array([40, 100, 100]), np.array([80, 255, 255]))

        pixel_rossi = cv2.countNonZero(maschera_rosso)
        pixel_verdi = cv2.countNonZero(maschera_verde)

        if pixel_rossi > 500:
            stato = "rosso"
            messaggio = "Semaforo rosso. Fermati."
        elif pixel_verdi > 500:
            stato = "verde"
            messaggio = "Semaforo verde. Puoi attraversare."
        else:
            stato = "non rilevato"
            messaggio = "Nessun semaforo rilevato."

        return jsonify({
            "stato_semaforo": stato,
            "messaggio_vocale": messaggio,
            "pixel_rossi": pixel_rossi,
            "pixel_verdi": pixel_verdi
        })

    except Exception as e:
        return jsonify({"errore": str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)