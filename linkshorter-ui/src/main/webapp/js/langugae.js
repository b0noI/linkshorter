var lang_key = "lang";
var ukr_key = "ukr";
var rus_key = "rus";
var eng_key = "eng";

$(document).ready(function() {
    setLanguage();

    $("#eng").click(function () {
        setLangEng();
        document.title = 'Shorter';
    });

    $("#rus").click(function () {
        setLangRus();
        document.title = 'Ужиматель';
    });

    $("#ukr").click(function () {
        setLangUkr();
        document.title = 'Скорочувальник';
    });
});

function setLanguage() {
    var lang = getCookie(lang_key);

    if (!lang) {
        setLangUkr();
        return;
    }

    if (lang === ukr_key)
        $(".ukr").show();
    else
        $(".ukr").hide();

    if (lang === eng_key)
        $(".eng").show();
    else
        $(".eng").hide();

    if (lang === rus_key)
        $(".rus").show();
    else
        $(".rus").hide();
}

function setLangUkr() {
    saveLanguage(ukr_key);
    $(".eng").hide();
    $(".ukr").show();
    $(".rus").hide();
}

function setLangRus() {
    saveLanguage(rus_key);
    $(".eng").hide();
    $(".ukr").hide();
    $(".rus").show();
}

function setLangEng() {
    saveLanguage(eng_key);
    $(".eng").show();
    $(".ukr").hide();
    $(".rus").hide();
}

function saveLanguage(lang) {
    setCookie(lang_key, lang, 100);
}

function setCookie(c_name,value,exdays)
{
    var exdate=new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
    document.cookie=c_name + "=" + c_value;
}

function getCookie(c_name)
{
    var c_value = document.cookie;
    var c_start = c_value.indexOf(" " + c_name + "=");
    if (c_start == -1)
    {
        c_start = c_value.indexOf(c_name + "=");
    }
    if (c_start == -1)
    {
        c_value = null;
    }
    else
    {
        c_start = c_value.indexOf("=", c_start) + 1;
        var c_end = c_value.indexOf(";", c_start);
        if (c_end == -1)
        {
            c_end = c_value.length;
        }
        c_value = unescape(c_value.substring(c_start,c_end));
    }
    return c_value;
}