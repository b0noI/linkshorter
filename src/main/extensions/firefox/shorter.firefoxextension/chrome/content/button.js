var cache = {};

function handleOnClick() {
}

function handleOnLoad() {
    var iframe = document.getElementById("lcoua-iframe");
    var doc = iframe.contentWindow.document;
    var input = doc.getElementById("lcoua-url");
    var qr_div = doc.getElementById("lcoua-qr");
    if ( input ) {
        var shortUrl = getShortUrl(window.content.location.href);
        input.value = shortUrl;
        input.select();
        var qr = qrcode(4, 'M');
        qr.addData(shortUrl);
        qr.make();
        qr_div.innerHTML = qr.createImgTag();
    } else { 
        return false; 
    }
}

function getShortUrl(fullUrl) {
    var url = "";
    if (cache[fullUrl]) {
        url = cache[fullUrl];
    } else {
        var xmlhttp = new XMLHttpRequest();
        var respCode = new RegExp(/^[2-3]\d{2}/);

        xmlhttp.onreadystatechange=function()
          {
          if (xmlhttp.readyState == 4 && respCode.test(xmlhttp.status))
            {
                url = "http://l.co.ua/" + xmlhttp.responseText;
            }  else {
                url = "Failed to get short URL: " + xmlhttp.responseText + " respCode: " + xmlhttp.status;
            }
          }
        xmlhttp.open("POST", "http://l.co.ua/rest/postUrl", false);
        xmlhttp.send(fullUrl);
        cache[fullUrl] = url;
    }
    return url;
}
