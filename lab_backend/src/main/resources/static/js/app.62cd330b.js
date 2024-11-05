(function () {
    "use strict";
    var e = {
        1760: function (e, t, a) {
            var n = a(5130), r = a(6768), o = a.p + "img/mangom_ggozil.5a7cf0f5.png",
                l = a.p + "img/mangom_typing.2493ebd4.png";
            const u = (0, r.Lk)("div", null, null, -1),
                i = (0, r.Lk)("div", null, [(0, r.Lk)("h1", null, "Nakji World")], -1),
                v = (0, r.Lk)("div", null, [(0, r.Lk)("img", {class: "mangom-logo", src: o})], -1), s = {class: "main"},
                c = (0, r.Lk)("img", {class: "mangom-cute", src: l}, null, -1);

            function b(e, t, a, n, o, l) {
                const b = (0, r.g2)("HelloWorld");
                return (0, r.uX)(), (0, r.CE)(r.FK, null, [u, i, v, (0, r.Lk)("div", s, [c, (0, r.bF)(b, {msg: "Welcome to Your Vue.js App"})])], 64)
            }

            var d = a(4232);
            const p = {class: "hello"},
                f = (0, r.Fv)('<p data-v-b9167eee> For a guide and recipes on how to configure / customize this project,<br data-v-b9167eee> check out the <a href="https://cli.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>vue-cli documentation</a>. </p><h3 data-v-b9167eee>Installed CLI Plugins</h3><ul data-v-b9167eee><li data-v-b9167eee><a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-babel" target="_blank" rel="noopener" data-v-b9167eee>babel</a></li><li data-v-b9167eee><a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-eslint" target="_blank" rel="noopener" data-v-b9167eee>eslint</a></li></ul><h3 data-v-b9167eee>Essential Links</h3><ul data-v-b9167eee><li data-v-b9167eee><a href="https://vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>Core Docs</a></li><li data-v-b9167eee><a href="https://forum.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>Forum</a></li><li data-v-b9167eee><a href="https://chat.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>Community Chat</a></li><li data-v-b9167eee><a href="https://twitter.com/vuejs" target="_blank" rel="noopener" data-v-b9167eee>Twitter</a></li><li data-v-b9167eee><a href="https://news.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>News</a></li></ul><h3 data-v-b9167eee>Ecosystem</h3><ul data-v-b9167eee><li data-v-b9167eee><a href="https://router.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>vue-router</a></li><li data-v-b9167eee><a href="https://vuex.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>vuex</a></li><li data-v-b9167eee><a href="https://github.com/vuejs/vue-devtools#vue-devtools" target="_blank" rel="noopener" data-v-b9167eee>vue-devtools</a></li><li data-v-b9167eee><a href="https://vue-loader.vuejs.org" target="_blank" rel="noopener" data-v-b9167eee>vue-loader</a></li><li data-v-b9167eee><a href="https://github.com/vuejs/awesome-vue" target="_blank" rel="noopener" data-v-b9167eee>awesome-vue</a></li></ul>', 7);

            function h(e, t, a, n, o, l) {
                return (0, r.uX)(), (0, r.CE)("div", p, [(0, r.Lk)("h1", null, (0, d.v_)(a.msg), 1), f])
            }

            var g = {name: "HelloWorld", props: {msg: String}}, m = a(1241);
            const k = (0, m.A)(g, [["render", h], ["__scopeId", "data-v-b9167eee"]]);
            var j = k, _ = {name: "App", components: {HelloWorld: j}};
            const w = (0, m.A)(_, [["render", b]]);
            var y = w;
            (0, n.Ef)(y).mount("#app")
        }
    }, t = {};

    function a(n) {
        var r = t[n];
        if (void 0 !== r) return r.exports;
        var o = t[n] = {exports: {}};
        return e[n].call(o.exports, o, o.exports, a), o.exports
    }

    a.m = e, function () {
        var e = [];
        a.O = function (t, n, r, o) {
            if (!n) {
                var l = 1 / 0;
                for (s = 0; s < e.length; s++) {
                    n = e[s][0], r = e[s][1], o = e[s][2];
                    for (var u = !0, i = 0; i < n.length; i++) (!1 & o || l >= o) && Object.keys(a.O).every((function (e) {
                        return a.O[e](n[i])
                    })) ? n.splice(i--, 1) : (u = !1, o < l && (l = o));
                    if (u) {
                        e.splice(s--, 1);
                        var v = r();
                        void 0 !== v && (t = v)
                    }
                }
                return t
            }
            o = o || 0;
            for (var s = e.length; s > 0 && e[s - 1][2] > o; s--) e[s] = e[s - 1];
            e[s] = [n, r, o]
        }
    }(), function () {
        a.n = function (e) {
            var t = e && e.__esModule ? function () {
                return e["default"]
            } : function () {
                return e
            };
            return a.d(t, {a: t}), t
        }
    }(), function () {
        a.d = function (e, t) {
            for (var n in t) a.o(t, n) && !a.o(e, n) && Object.defineProperty(e, n, {enumerable: !0, get: t[n]})
        }
    }(), function () {
        a.g = function () {
            if ("object" === typeof globalThis) return globalThis;
            try {
                return this || new Function("return this")()
            } catch (e) {
                if ("object" === typeof window) return window
            }
        }()
    }(), function () {
        a.o = function (e, t) {
            return Object.prototype.hasOwnProperty.call(e, t)
        }
    }(), function () {
        a.p = "/"
    }(), function () {
        var e = {524: 0};
        a.O.j = function (t) {
            return 0 === e[t]
        };
        var t = function (t, n) {
            var r, o, l = n[0], u = n[1], i = n[2], v = 0;
            if (l.some((function (t) {
                return 0 !== e[t]
            }))) {
                for (r in u) a.o(u, r) && (a.m[r] = u[r]);
                if (i) var s = i(a)
            }
            for (t && t(n); v < l.length; v++) o = l[v], a.o(e, o) && e[o] && e[o][0](), e[o] = 0;
            return a.O(s)
        }, n = self["webpackChunkvue_nakji_lab"] = self["webpackChunkvue_nakji_lab"] || [];
        n.forEach(t.bind(null, 0)), n.push = t.bind(null, n.push.bind(n))
    }();
    var n = a.O(void 0, [504], (function () {
        return a(1760)
    }));
    n = a.O(n)
})();
//# sourceMappingURL=app.62cd330b.js.map