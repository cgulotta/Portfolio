/* This will save the document values */
chrome.extension.savedText = [];
chrome.extension.currentIndex = 0;
chrome.extension.savedImages = [];
chrome.extension.imageIndex = 0;

var imgURL = 'http://science-all.com/images/banana/banana-02.jpg';

chrome.extension.onRequest.addListener(function(request, sender, sendResponse) {
 if (request.action == "censor") {
   censor();
 }
 if (request.action == "refresh") {
   restore();
 }
 if (request.action == "country") {
   // console.log(request.country.options[request.country.selectedIndex]);
   updateCensorship(request.country);
 }
});

chrome.runtime.onMessage.addListener(function (msg, sender, sendResponse) {
  console.log(msg);
});


function restore() {
	setTimeout(function () {
    chrome.extension.currentIndex = 0;
    chrome.extension.imageIndex = 0;
    restoreTree(document.body);
    chrome.extension.savedImages = [];
    chrome.extension.savedText = [];
	}, 1000);
}

/* This function restores the text for the document body */
function restoreTree(node) {
  var child, next;

	switch ( node.nodeType ) {
		case 1:
    if (node.tagName === 'IMG') {
      /* Replace image with another strange one */
      // console.log('Found image');
      restoreImage(node);
    } // Element
		case 9:  // Document
		case 11: // Document fragment
      child = node.firstChild;
			while ( child )
			{
				next = child.nextSibling;
				restoreTree(child);
				child = next;
			}
			break;

		case 3: // Text node
			restoreText(node);
			break;
	}
}

function restoreText(textNode) {
	textNode.nodeValue = chrome.extension.savedText[chrome.extension.currentIndex];
  chrome.extension.currentIndex = chrome.extension.currentIndex + 1;
}

function restoreImage(imageNode) {
	imageNode.src = chrome.extension.savedImages[chrome.extension.imageIndex];
  chrome.extension.imageIndex = chrome.extension.imageIndex + 1;
}

/* This function removes words from the document body */
function censor() {
	setTimeout(function () {
	  walk(document.body);
	}, 1000);
}

function walk(node) {
	var child, next;

	switch ( node.nodeType ) {
		case 1:
      if (node.tagName === 'IMG') {
        /* Replace image with another strange one */
        // console.log('Found image');
        replaceImage(node);
      }// Element
		case 9:  // Document
		case 11: // Document fragment
      child = node.firstChild;
			while ( child )
			{
				next = child.nextSibling;
				walk(child);
				child = next;
			}
			break;

		case 3: // Text node
			handleText(node);
			break;
	}
}

function replaceImage(imageNode) {
  chrome.extension.savedImages.push(imageNode.src);
  imageNode.src = imgURL;
}

function handleText(textNode) {
  var noun_arr = Array("time","issue","year","side","people","kind","way","head","day","house","man","service","thing","friend","woman","father","life","power","child","hour","world"	,"game","school"	,"line","state"	,"end","family"	,"member","student"	,"law","group"	,"car","country"	,"city","problem"	,"community","hand"	,"name","part"	,"president","place"	,"team","case"	,"minute","week"	,"idea","company"	,"kid","system"	,"body","program"	,"information","question"	,"back","work"	,"parent","government" ,"face","number"	,"others","night"	,"level","Mr"	,"office","point"	,"door","home"	,"health","water"	,"person","room"	,"art","mother"	,"war","area"	,"history","money"	,"party","storey"	,"result","fact"	,"change","month"	,"morning","lot"	,"reason","right"	,"research","study"	,"girl","book"	,"guy","eye"	,"food","job"	,"moment","word"	,"air","business"	,"teacher");
  /* Save the text value */
  chrome.extension.savedText.push(textNode.nodeValue);
  var v = textNode.nodeValue;

  for (var i = 0; i < noun_arr.length; i++) {
    var s = "";
		for (var j = 0; j < noun_arr[i].length; j++) {
			s+= "█";
		}
    v = v.replace(" "+noun_arr[i]+" ", " "+s+" ");
  }
	textNode.nodeValue = v;
}
