document.addEventListener("DOMContentLoaded", function () {
    let CommonChatObj = Object.create(CommonChat);

    CommonChatObj.setTag("body");
    CommonChatObj.timeout(0);
    CommonChatObj.execute();
});



var CommonChat = {
    time: 0,
    tag: "body",
    site: location.hostname,
    server: "http://localhost:8080/",
    linkId: "CommonChat-link-id",
    frameId: "CommonChat",
    Cookies: "CommonChatUser",

    timeout: function (seconds) {
        this.time = seconds;
    },

    setTag: function (tag) {
        this.tag = tag;
    },



    execute: function () {
        let obj = this;

        setTimeout(function () {
            obj.run();
        }, this.time);
    },

    run: function () {
        let append = document.querySelector(this.tag);
        let cookie = this.cookie.get(this.Cookies);

        if (append == null || append == undefined)
            return ;

        this.linkFrameCSS();
        this.createIframe(cookie);
    },

    createIframe: function (username) {
        let button = document.createElement("div");
        button.id = this.frameId + "-circle";
        button.innerHTML = '<img style="width: 65px; margin-top: 13px; margin-left: 12px;" src="' + this.server + "main/chat.svg" + '">';
        document.querySelector(this.tag).appendChild(button);

        let wrap = document.createElement("div");
        wrap.id = this.frameId + "-box";
        document.querySelector(this.tag).appendChild(wrap);

        let head = document.createElement("div");
        head.id = this.frameId + "-header";
        head.innerHTML = "Чат<span id=\"CommonChat-toggle\">&#10006;</span>";
        wrap.appendChild(head);


        let window = document.createElement("div");
        window.id = this.frameId + "-body";
        wrap.appendChild(window);

        if (username == null || username == undefined)
        {
            window.innerHTML = '<div id="CommonChat-hello"><div id="CommonChat-group"><input type="text" id="CommonChat-input" required><span id="CommonChat-bar"></span><label id="CommonChat-label">Как вас зовут?</label></div><button>Начать!</button></div>';
            let obj = this;

            document.querySelector("#" + this.frameId + "-hello button").addEventListener("click", function () {
                obj.validateUsername();
            });
        }
        else
        {
            this.initIframe(username, 0);
        }


        document.querySelector("#CommonChat-circle").addEventListener("click", function ()
        {
            setTimeout( function()
            {
                let frame = document.querySelector("#CommonChat-iframe");

                document.querySelector("#CommonChat-circle").style.display = 'none';
                document.querySelector("#CommonChat-box").style.display = 'block';

                if (frame != undefined && frame != null && frame)
                    frame.contentWindow.scrollTo(0, frame.contentWindow.document.body.scrollHeight);
            }, 100);
        });

        document.querySelector("#CommonChat-toggle").addEventListener("click", function ()
        {
            setTimeout( function()
            {
                document.querySelector("#CommonChat-circle").style.display = 'block';
                document.querySelector("#CommonChat-box").style.display = 'none';
            }, 100);
        });       
    },

    validateUsername: function () {
        let input = document.querySelector("#" + this.frameId + "-input");

        if (!input || !input.value || input.value == "" || input.value == null || !input.value.length || input.value.trim() == null || input.value.trim() == "")
            return ;

        this.cookie.set(this.Cookies, input.value.trim(), 7);
        this.initIframe(input.value, 1);
    },

    initIframe: function (username, flag) {
        if (flag && flag > 0)
        {
            let rm = document.querySelector("#" + this.frameId + "-hello");
            rm.outerHTML = "";
            delete rm;
        }

        let frame = document.createElement("iframe");
        frame.id = this.frameId + "-iframe";
        frame.setAttribute("src", this.server + "window?site=" + this.site + "&username=" + username);
        document.querySelector("#" + this.frameId + "-body").appendChild(frame);
    },

    linkFrameCSS: function () {
        let css = document.createElement("link");

        css.id = this.linkId;
        css.rel = 'stylesheet';
        css.href = this.server + "main/CommonChatCSS.css";
        document.querySelector(this.tag).appendChild(css);
    },

    cookie: {
        set: function setCookie(name, value, days) {
            let expires = "";
            let date = new Date();

            if (days) {
                date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
                expires = "; expires=" + date.toUTCString();
            }
            document.cookie = name + "=" + (value || "") + expires + "; path=/";
        },
        get: function (name) {
            let nameEQ = name + "=";
            let ca = document.cookie.split(';');

            for(let i = 0;i < ca.length;i++) {
                let c = ca[i];

                while (c.charAt(0) == ' ')
                    c = c.substring(1,c.length);

                if (c.indexOf(nameEQ) == 0)
                    return c.substring(nameEQ.length,c.length);
            }
            return null;
        }
    }
};