from flask import Flask, request, send_file
from flask_cors import CORS
import gc
import os
import torch

from image_gen import image_gen 


app = Flask(__name__)

CORS(app)

@app.route('/api', methods=['POST'])
def receive_option():
    prompt = request.form.get('prompt')
    print("/////////////////////////////////")
    print(prompt)
    
    image_file = image_gen(prompt)

    if image_file and os.path.exists(image_file):
        
        return send_file(image_file, mimetype='image/png')
    else:
        return "File not found", 404

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
