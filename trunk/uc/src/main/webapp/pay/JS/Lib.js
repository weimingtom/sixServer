/**
 * Created by Administrator on 2015/1/20.
 */
(function (d) {
    d.expr[":"].icontains = function (b, a, c) {
        return 0 <= d(b).text().toUpperCase().indexOf(c[3].toUpperCase())
    };
    var e = function (b, a, c) {
        c && (c.stopPropagation(), c.preventDefault());
        this.$element = d(b);
        this.$menu = this.$button = this.$newElement = null;
        this.options = d.extend({}, d.fn.selectpicker.defaults, this.$element.data(), "object" == typeof a && a);
        null == this.options.title && (this.options.title = this.$element.attr("title"));
        this.val = e.prototype.val;
        this.render = e.prototype.render;
        this.refresh = e.prototype.refresh;
        this.setStyle = e.prototype.setStyle;
        this.selectAll = e.prototype.selectAll;
        this.deselectAll = e.prototype.deselectAll;
        this.init()
    };
    e.prototype = {constructor: e, init: function () {
        this.$element.hide();
        this.multiple = this.$element.prop("multiple");
        var b = this.$element.attr("id");
        this.$newElement = this.createView();
        this.$element.after(this.$newElement);
        this.$menu = this.$newElement.find("> .dropdown-menu");
        this.$button = this.$newElement.find("> button");
        this.$searchbox = this.$newElement.find("input");
        if (void 0 !== b) {
            var a =
                this;
            this.$button.attr("data-id", b);
            d('label[for="' + b + '"]').click(function (b) {
                b.preventDefault();
                a.$button.focus()
            })
        }
        this.checkDisabled();
        this.clickListener();
        this.liveSearchListener();
        this.render();
        this.liHeight();
        this.setStyle();
        this.setWidth();
        this.options.container && this.selectPosition();
        this.$menu.data("this", this);
        this.$newElement.data("this", this)
    }, createDropdown: function () {
        return d("<div class='btn-group select" + (this.multiple ? " show-tick" : "") + "'><button type='button' class='btn dropdown-toggle' data-toggle='dropdown'><div class='filter-option pull-left'></div>&nbsp;<div class='caret'></div></button><div class='dropdown-menu open'>" +
            (this.options.header ? '<h3 class="popover-title">' + this.options.header + '<button type="button" class="close" aria-hidden="true">&times;</button></h3>' : "") + (this.options.liveSearch ? '<div class="select-searchbox"><input type="text" class="input-block-level form-control" /></div>' : "") + "<ul class='dropdown-menu inner' role='menu'></ul></div></div>")
    }, createView: function () {
        var b = this.createDropdown(), a = this.createLi();
        b.find("ul").append(a);
        return b
    }, reloadLi: function () {
        this.destroyLi();
        var b = this.createLi();
        this.$menu.find("ul").append(b)
    }, destroyLi: function () {
        this.$menu.find("li").remove()
    }, createLi: function () {
        var b = this, a = [], c = "";
        this.$element.find("option").each(function () {
            var c = d(this), f = c.attr("class") || "", j = c.attr("style") || "", h = c.data("content") ? c.data("content") : c.html(), g = void 0 !== c.data("subtext") ? '<small class="muted">' + c.data("subtext") + "</small>" : "", i = void 0 !== c.data("icon") ? '<i class="glyphicon ' + c.data("icon") + '"></i> ' : "";
            if ("" !== i && (c.is(":disabled") || c.parent().is(":disabled")))i = "<span>" +
                i + "</span>";
            c.data("content") || (h = i + '<span class="text">' + h + g + "</span>");
            b.options.hideDisabled && (c.is(":disabled") || c.parent().is(":disabled")) ? a.push('<a style="min-height: 0; padding: 0"></a>') : c.parent().is("optgroup") && !0 != c.data("divider") ? 0 == c.index() ? (g = c.parent().attr("label"), i = void 0 !== c.parent().data("subtext") ? '<small class="muted">' + c.parent().data("subtext") + "</small>" : "", g = (c.parent().data("icon") ? '<i class="' + c.parent().data("icon") + '"></i> ' : "") + '<span class="text">' + g + i + "</span>",
                    0 != c[0].index ? a.push('<div class="div-contain"><div class="divider"></div></div><dt>' + g + "</dt>" + b.createA(h, "opt " + f, j)) : a.push("<dt>" + g + "</dt>" + b.createA(h, "opt " + f, j))) : a.push(b.createA(h, "opt " + f, j)) : !0 == c.data("divider") ? a.push('<div class="div-contain"><div class="divider"></div></div>') : !0 == d(this).data("hidden") ? a.push("") : a.push(b.createA(h, f, j))
        });
        d.each(a, function (a, b) {
            c += "<li rel=" + a + ">" + b + "</li>"
        });
        !this.multiple && (0 == this.$element.find("option:selected").length && !this.options.title) &&
        this.$element.find("option").eq(0).prop("selected", !0).attr("selected", "selected");
        return d(c)
    }, createA: function (b, a, c) {
        return'<a tabindex="0" class="' + a + '" style="' + c + '">' + b + '<i class="glyphicon glyphicon-ok icon-ok check-mark"></i></a>'
    }, render: function () {
        var b = this;
        this.$element.find("option").each(function (a) {
            b.setDisabled(a, d(this).is(":disabled") || d(this).parent().is(":disabled"));
            b.setSelected(a, d(this).is(":selected"))
        });
        this.tabIndex();
        var a = this.$element.find("option").eq(this.$element.get(0).selectedIndex).map(function () {
            var a =
                d(this), c = a.data("icon") && b.options.showIcon ? '<i class="glyphicon ' + a.data("icon") + '"></i> ' : "", g;
            g = b.options.showSubtext && a.attr("data-subtext") && !b.multiple ? ' <small class="muted">' + a.data("subtext") + "</small>" : "";
            return a.data("content") && b.options.showContent ? a.data("content") : void 0 != a.attr("title") ? a.attr("title") : c + a.html() + g
        }).toArray(), c = !this.multiple ? a[0] : a.join(", ");
        if (this.multiple && -1 < this.options.selectedTextFormat.indexOf("count")) {
            var k = this.options.selectedTextFormat.split(">"),
                f = this.options.hideDisabled ? ":not([disabled])" : "";
            if (1 < k.length && a.length > k[1] || 1 == k.length && 2 <= a.length)c = this.options.countSelectedText.replace("{0}", a.length).replace("{1}", this.$element.find('option:not([data-divider="true"]):not([data-hidden="true"])' + f).length)
        }
        c || (c = void 0 != this.options.title ? this.options.title : this.options.noneSelectedText);
        this.$newElement.find(".filter-option").html(c)
    }, setStyle: function (b, a) {
        this.$element.attr("class") && this.$newElement.addClass(this.$element.attr("class").replace(/selectpicker|mobile-device/gi,
            ""));
        var c = b ? b : this.options.style;
        "add" == a ? this.$button.addClass(c) : "remove" == a ? this.$button.removeClass(c) : (this.$button.removeClass(this.options.style), this.$button.addClass(c))
    }, liHeight: function () {
        var b = this.$newElement.clone();
        b.appendTo("body");
        var a = b.addClass("open").find("> .dropdown-menu"), c = a.find("li > a").outerHeight(), d = this.options.header ? a.find(".popover-title").outerHeight() : 0, a = this.options.header ? a.find(".select-searchbox").outerHeight() : 0;
        b.remove();
        this.$newElement.data("liHeight",
            c).data("headerHeight", d).data("searchHeight", a)
    }, setSize: function () {
        var b = this, a = this.$menu, c = a.find(".inner");
        c.find("li > a");
        var k = this.$newElement.outerHeight(), f = this.$newElement.data("liHeight"), j = this.$newElement.data("headerHeight"), h = this.$newElement.data("searchHeight"), g = a.find("li .divider").outerHeight(!0), i = parseInt(a.css("padding-top")) + parseInt(a.css("padding-bottom")) + parseInt(a.css("border-top-width")) + parseInt(a.css("border-bottom-width")), e = this.options.hideDisabled ? ":not(.disabled)" :
            "", q = d(window), n = i + parseInt(a.css("margin-top")) + parseInt(a.css("margin-bottom")) + 2, l, m, o, r = function () {
            m = b.$newElement.offset().top - q.scrollTop();
            o = q.height() - m - k
        };
        r();
        this.options.header && a.css("padding-top", 0);
        "auto" == this.options.size ? (g = function () {
            var d;
            r();
            l = o - n;
            b.$newElement.toggleClass("dropup", m > o && l - n < a.height() && b.options.dropupAuto);
            b.$newElement.hasClass("dropup") && (l = m - n);
            d = 3 < a.find("li").length + a.find("dt").length ? 3 * f + n - 2 : 0;
            a.css({"max-height": l + "px", overflow: "hidden", "min-height": d +
                "px"});
            c.css({"max-height": l - j - h - i + "px", "overflow-y": "auto", "min-height": d - i + "px"})
        }, g(), d(window).resize(g), d(window).scroll(g)) : this.options.size && ("auto" != this.options.size && a.find("li" + e).length > this.options.size) && (e = a.find("li" + e + " > *").filter(":not(.div-contain)").slice(0, this.options.size).last().parent().index(), e = a.find("li").slice(0, e + 1).find(".div-contain").length, l = f * this.options.size + e * g + i, this.$newElement.toggleClass("dropup", m > o && l < a.height() && this.options.dropupAuto), a.css({"max-height": l +
            j + h + "px", overflow: "hidden"}), c.css({"max-height": l - i + "px", "overflow-y": "auto"}))
    }, setWidth: function () {
        if ("auto" == this.options.width) {
            this.$menu.css("min-width", "0");
            var b = this.$newElement.clone().appendTo("body"), a = b.find("> .dropdown-menu").css("width");
            b.remove();
            this.$newElement.css("width", a)
        } else"fit" == this.options.width ? (this.$menu.css("min-width", ""), this.$newElement.css("width", "").addClass("fit-width")) : this.options.width ? (this.$menu.css("min-width", ""), this.$newElement.css("width", this.options.width)) :
            (this.$menu.css("min-width", ""), this.$newElement.css("width", ""));
        this.$newElement.hasClass("fit-width") && "fit" !== this.options.width && this.$newElement.removeClass("fit-width")
    }, selectPosition: function () {
        var b = this, a = d("<div />"), c, e, f = function (b) {
            a.addClass(b.attr("class")).toggleClass("dropup", b.hasClass("dropup"));
            c = b.offset();
            e = b.hasClass("dropup") ? 0 : b[0].offsetHeight;
            a.css({top: c.top + e, left: c.left, width: b[0].offsetWidth, position: "absolute"})
        };
        this.$newElement.on("click", function () {
            f(d(this));
            a.appendTo(b.options.container);
            a.toggleClass("open", !d(this).hasClass("open"));
            a.append(b.$menu)
        });
        d(window).resize(function () {
            f(b.$newElement)
        });
        d(window).on("scroll", function () {
            f(b.$newElement)
        });
        d("html").on("click", function (c) {
            1 > d(c.target).closest(b.$newElement).length && a.removeClass("open")
        })
    }, mobile: function () {
        this.$element.addClass("mobile-device").appendTo(this.$newElement);
        this.options.container && this.$menu.hide()
    }, refresh: function () {
        this.reloadLi();
        this.render();
        this.setWidth();
        this.setStyle();
        this.checkDisabled();
        this.liHeight()
    }, update: function () {
        this.reloadLi();
        this.setWidth();
        this.setStyle();
        this.checkDisabled();
        this.liHeight()
    }, setSelected: function (b, a) {
        this.$menu.find("li").eq(b).toggleClass("selected", a)
    }, setDisabled: function (b, a) {
        a ? this.$menu.find("li").eq(b).addClass("disabled").find("a").attr("href", "#").attr("tabindex", -1) : this.$menu.find("li").eq(b).removeClass("disabled").find("a").removeAttr("href").attr("tabindex", 0)
    }, isDisabled: function () {
        return this.$element.is(":disabled")
    },
        checkDisabled: function () {
            var b = this;
            this.isDisabled() ? this.$button.addClass("disabled").attr("tabindex", -1) : (this.$button.hasClass("disabled") && this.$button.removeClass("disabled"), -1 == this.$button.attr("tabindex") && (this.$element.data("tabindex") || this.$button.removeAttr("tabindex")));
            this.$button.click(function () {
                return!b.isDisabled()
            })
        }, tabIndex: function () {
            this.$element.is("[tabindex]") && (this.$element.data("tabindex", this.$element.attr("tabindex")), this.$button.attr("tabindex", this.$element.data("tabindex")))
        },
        clickListener: function () {
            var b = this;
            d("body").on("touchstart.dropdown", ".dropdown-menu", function (a) {
                a.stopPropagation()
            });
            this.$newElement.on("click", function () {
                b.setSize()
            });
            this.$menu.on("click", "li a", function (a) {
                var c = d(this).parent().index();
                d(this).parent();
                var e = b.$element.val();
                b.multiple && a.stopPropagation();
                a.preventDefault();
                !b.isDisabled() && !d(this).parent().hasClass("disabled") && (a = b.$element.find("option").eq(c), b.multiple ? (c = a.prop("selected"), a.prop("selected", !c)) : b.$element.get(0).selectedIndex =
                    c, b.$button.focus(), e != b.$element.val() && b.$element.change())
            });
            this.$menu.on("click", "li.disabled a, li dt, li .div-contain, h3.popover-title", function (a) {
                a.target == this && (a.preventDefault(), a.stopPropagation(), b.$button.focus())
            });
            this.$searchbox.on("click", function (a) {
                a.stopPropagation()
            });
            this.$element.change(function () {
                b.render()
            })
        }, liveSearchListener: function () {
            var b = this;
            this.$newElement.on("click.dropdown.data-api", function () {
                b.options.liveSearch && setTimeout(function () {
                        b.$searchbox.focus()
                    },
                    10)
            });
            this.$searchbox.on("input", function () {
                b.$searchbox.val() ? b.$menu.find("li").show().not(":icontains(" + b.$searchbox.val() + ")").hide() : b.$menu.find("li").show()
            })
        }, val: function (b) {
            return void 0 != b ? (this.$element.val(b), this.$element.change(), this.$element) : this.$element.val()
        }, selectAll: function () {
            this.$element.find("option").prop("selected", !0).attr("selected", "selected");
            this.render()
        }, deselectAll: function () {
            this.$element.find("option").prop("selected", !1).removeAttr("selected");
            this.render()
        },
        keydown: function (b) {
            var a, c, e, f, j, h, g;
            a = d(this).parent();
            c = a.data("this");
            c.options.container && (a = c.$menu);
            a = d("[role=menu] li:not(.divider):visible a", a);
            if (a.length) {
                if (/(38|40)/.test(b.keyCode))c = a.index(a.filter(":focus")), f = a.parent(":not(.disabled)").first().index(), j = a.parent(":not(.disabled)").last().index(), e = a.eq(c).parent().nextAll(":not(.disabled)").eq(0).index(), h = a.eq(c).parent().prevAll(":not(.disabled)").eq(0).index(), g = a.eq(e).parent().prevAll(":not(.disabled)").eq(0).index(), 38 ==
                    b.keyCode && (c != g && c > h && (c = h), c < f && (c = f)), 40 == b.keyCode && (c != g && c < e && (c = e), c > j && (c = j), -1 == c && (c = 0)), a.eq(c).focus(); else {
                    var i = {48: "0", 49: "1", 50: "2", 51: "3", 52: "4", 53: "5", 54: "6", 55: "7", 56: "8", 57: "9", 59: ";", 65: "a", 66: "b", 67: "c", 68: "d", 69: "e", 70: "f", 71: "g", 72: "h", 73: "i", 74: "j", 75: "k", 76: "l", 77: "m", 78: "n", 79: "o", 80: "p", 81: "q", 82: "r", 83: "s", 84: "t", 85: "u", 86: "v", 87: "w", 88: "x", 89: "y", 90: "z", 96: "0", 97: "1", 98: "2", 99: "3", 100: "4", 101: "5", 102: "6", 103: "7", 104: "8", 105: "9"}, p = [];
                    a.each(function () {
                        d(this).parent().is(":not(.disabled)") &&
                            d.trim(d(this).text().toLowerCase()).substring(0, 1) == i[b.keyCode] && p.push(d(this).parent().index())
                    });
                    c = d(document).data("keycount");
                    c++;
                    d(document).data("keycount", c);
                    d.trim(d(":focus").text().toLowerCase()).substring(0, 1) != i[b.keyCode] ? (c = 1, d(document).data("keycount", c)) : c >= p.length && d(document).data("keycount", 0);
                    a.eq(p[c - 1]).focus()
                }
                /(13|32)/.test(b.keyCode) && (b.preventDefault(), d(":focus").click(), d(document).data("keycount", 0))
            }
        }, hide: function () {
            this.$newElement.hide()
        }, show: function () {
            this.$newElement.show()
        },
        destroy: function () {
            this.$newElement.remove();
            this.$element.remove()
        }};
    d.fn.selectpicker = function (b, a) {
        var c = arguments, k, f = this.each(function () {
            if (d(this).is("select")) {
                var f = d(this), h = f.data("selectpicker"), g = "object" == typeof b && b;
                if (h) {
                    if (g)for (var i in g)h.options[i] = g[i]
                } else f.data("selectpicker", h = new e(this, g, a));
                "string" == typeof b && (f = b, h[f]instanceof Function ? ([].shift.apply(c), k = h[f].apply(h, c)) : k = h.options[f])
            }
        });
        return void 0 != k ? k : f
    };
    d.fn.selectpicker.defaults = {style: "btn-default", size: "auto",
        title: null, selectedTextFormat: "values", noneSelectedText: "Nothing selected", countSelectedText: "{0} of {1} selected", width: !1, container: !1, hideDisabled: !1, showSubtext: !1, showIcon: !0, showContent: !0, dropupAuto: !0, header: !1, liveSearch: !1};
    d(document).data("keycount", 0).on("keydown", "[data-toggle=dropdown], [role=menu]", e.prototype.keydown)
})(window.jQuery);
