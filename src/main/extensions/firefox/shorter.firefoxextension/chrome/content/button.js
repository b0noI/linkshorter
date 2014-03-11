function handleOnClick() {
    document.getElementById("lcoua-panel").openPopup;
}

function handleOnLoad() {
    var iframe = document.getElementById("lcoua-iframe");
//    iframe.setAttribute("src","chrome://lcoua/content/iframe.html");
    iframe.setAttribute("visible",true);
    iframe.contentDocument.location.reload(true);
//        var iframe = document.getElementById("lcoua-iframe");
//        var doc = iframe.contentWindow.document;
//    if (!doc.getElementById("lcoua-url")) {
//        var input = doc.createElement("input");
//        input.type = "text";
//        input.size = 15;
//        input.id = "lcoua-url";
//        input.value = window.content.location.href;
//        doc.body.appendChild(input);
//    }
}

