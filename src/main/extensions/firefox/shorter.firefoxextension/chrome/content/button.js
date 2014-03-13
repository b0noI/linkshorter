function handleOnClick() {
}

function handleOnLoad() {
    var iframe = document.getElementById("lcoua-iframe");
//        var iframe = document.getElementById("lcoua-iframe");
        var doc = iframe.contentWindow.document;
        var input = doc.getElementById("lcoua-url");
    if ( input ) {
//        input.type = "text";
//        input.size = 15;
//        input.id = "lcoua-url";
        input.value = window.content.location.href;
//        doc.body.appendChild(input);
    }
}

