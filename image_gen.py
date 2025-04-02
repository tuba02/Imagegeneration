import torch
from diffusers import DiffusionPipeline

pipe = DiffusionPipeline.from_pretrained(
    "cagliostrolab/animagine-xl-3.1", 
    torch_dtype=torch.float16, 
    use_safetensors=True, 
)
pipe.to('cuda')

def image_gen(option):
    prompt = 'best quality,' + option
    negative_prompt = "bad anatomy,long body, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, blurry, artist name, nsfw, lowres, (bad), fewer, extra, missing, jpeg artifacts, unfinished, displeasing, oldest, early, chromatic aberration, signature, artistic error, username, scan, [abstract]"

    image = pipe(
        prompt, 
        negative_prompt=negative_prompt, 
        width=896,
        height=1152,
        guidance_scale=6,
        num_inference_steps=15
    ).images[0]


    file_name = "image_gen.png" 

    image.save(file_name)
    return file_name
