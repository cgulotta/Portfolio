from django.shortcuts import render
import os
import json

def build_context():
    with open(os.path.join(os.path.dirname(__file__),"./static/page_data.json")) as f:
        context = json.load(f)
    context["slideshow"]=[]
    for f in os.listdir(os.path.join(os.path.dirname(__file__),"./static/images/slideshow")):
        context["slideshow"].append(f)
    return context

# Create your views here.
def LandingPageView(request):
    name='landing.html'
    context = build_context()
    return render(request,name,context)
