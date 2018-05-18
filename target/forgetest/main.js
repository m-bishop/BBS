(function() {

    //var wsUri = "ws://localhost:8080/forgetest/chat"; // local testing
    ////var wsUri = "ws://" + document.location.hostname +":8080"+ document.location.pathname + "chat"; // old
    var wsUri = "ws://" + document.location.hostname + ":8080" + document.location.pathname.substring(0,document.location.pathname.lastIndexOf('/')+1) + "chat"; //current
    console.log(wsUri);
    var _websocket = new WebSocket(wsUri);

    var _username = "";
    var $output;
    var _inited = false;
    var _locked = false;
    var _chatBuffer = [];
    var _buffer = [];
    var _obuffer = [];
    var _ibuffer = [];
    var _history = [];
    var _hindex = -1;
    var _lhindex = -1;

    var _input = "";

    var _commands = {

        chat: function(){
            if (_username == ""){
                return ("Must use setname <name> before chat!\n");
            }else {
                _websocket.send(_username+": "+_chatBuffer.substring(5, _chatBuffer.length));
                return ("\n");
            }
        },

        setname: function(name){
            if (name == ""){
                return ("Must enter a username!\n");
            } else {
                _username = name;
                return ("username set to: "+_username+"\n");
            }
        },

        clear: function() {
            return false;
        },


        help: function() {
            var out = [
                ' ---------------------------------------',
                '| help................This command      |',
                '| chat <message>......Send a message    |',
                '| setname <name>......Set name for chat |',
                '| clear...............Clear screen      |',
                ' ---------------------------------------',
                ''
            ];

            return out.join("\n");
        }

    };

    /////////////////////////////////////////////////////////////////
    // UTILS
    /////////////////////////////////////////////////////////////////
    function chunk(str, n) {
        var ret = [];
        var i;
        var len;

        for(i = 0, len = str.length; i < len; i += n) {
            ret.push(str.substr(i, n))
        }
        return ret
    };

    function commandInput(){
        var input = document.getElementById("textField").value;
        command(input);
    }

    function setSelectionRange(input, selectionStart, selectionEnd) {
        if (input.setSelectionRange) {
            //input.focus();
            input.setSelectionRange(selectionStart, selectionEnd);
        }
        else if (input.createTextRange) {
            var range = input.createTextRange();
            range.collapse(true);
            range.moveEnd('character', selectionEnd);
            range.moveStart('character', selectionStart);
            range.select();
        }
    }


    window.requestAnimFrame = (function(){
        return  window.requestAnimationFrame       ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame    ||
            function( callback ){
                window.setTimeout(callback, 1000/60);
            };
    })();

    /////////////////////////////////////////////////////////////////
    // SHELL
    /////////////////////////////////////////////////////////////////

    (function animloop(){
        requestAnimFrame(animloop);
        if (_input.length){
            $output.value += _input.substr(0,80);
            _input = _input.substr(80);
            update();
        }


        /*
                if ( _obuffer.length ) {
                for (i = 0; i < 80; i++){
                if ( _obuffer.length ){
                    $output.value += _obuffer.shift();
                }
        }

                // _locked = true;

                    update();
                } else {
                    if ( _ibuffer.length ) {
                        $output.value += _ibuffer.shift();
        $output.value += _ibuffer.shift();
        $output.value += _ibuffer.shift();
        $output.value += _ibuffer.shift();
        $output.value += _ibuffer.shift();

                        update();
                    }

                    _locked = false;
                    _inited = true;
                }
        */
    })();

    function print(input, lp) {
        input = chunk(input,80).join("\n");
        update();
        _input = input;
        _obuffer = _obuffer.concat(lp ? [input] : input.split(''));
    }

    function update() {
        //$output.focus();
        //setTimeout(function(){$output.focus();},0); // Chrome bug
        //setTimeout(function(){document.getElementById("textField").focus();},0);
        var l = $output.value.length;
        setSelectionRange($output, l, l);
        $output.scrollTop = $output.scrollHeight;
        document.getElementById("textField").focus();
    }

    function clear() {
        $output.value = '';
        _ibuffer = [];
        _obuffer = [];
        print("");
    }

    function command(cmd) {
        _websocket.send(cmd);
        /*
        print("\n");
        if ( cmd.length ) {
            var a = cmd.split(' ');
            var c = a.shift();
            _chatBuffer = cmd;
            if ( c in _commands ) {
                var result = _commands[c].apply(_commands, a);
                if ( result === false ) {
                    clear();
                } else {
                    print(result || "\n", true);
                }
            } else {
                print("Unknown command: " + cmd +"\n");
            }

            _history.push(cmd);
        }
        //print("\n\n" + _prompt());

        _hindex = -1;
        */
    }

    function nextHistory() {
        if ( !_history.length ) return;

        var insert;
        if ( _hindex == -1 ) {
            _hindex  = _history.length - 1;
            _lhindex = -1;
            insert   = _history[_hindex];
        } else {
            if ( _hindex > 1 ) {
                _lhindex = _hindex;
                _hindex--;
                insert = _history[_hindex];
            }
        }

        if ( insert ) {
            if ( _lhindex != -1 ) {
                var txt = _history[_lhindex];
                $output.value = $output.value.substr(0, $output.value.length - txt.length);
                update();
            }
            _buffer = insert.split('');
            _ibuffer = insert.split('');
        }
    }

    window.onload = function() {
        document.getElementById("screen").addEventListener('click',function()
        {
            if (document.getElementById("textField").value === ""){
                document.getElementById("textField").focus();
            } else {
                commandInput();
                document.getElementById("textField").value = "";
            }
        }  );

        /*
            document.getElementById("textField").focus();
                document.getElementById("textField").onkeydown = function(ev) {
                    var k = ev.which || ev.keyCode;
                if ( k == 13 ) {
                    commandInput();
                    document.getElementById("textField").value = "";
                }
                }
        */
        document.getElementById("textField").onkeypress = function(ev) {
            var k = ev.which || ev.keyCode;
            if ( k == 13 ) {
                commandInput();
                document.getElementById("textField").value = "";
                document.getElementById("textField").focus();
            }
        }

        $output = document.getElementById("output");
        $output.contentEditable = true;
        $output.spellcheck = false;
        $output.value = '';
        /*
                $output.onkeydown = function(ev) {
                    var k = ev.which || ev.keyCode;
                    var cancel = false;

                    if ( !_inited ) {
                        cancel = true;
                    } else {
                        if ( k == 9 ) {
                            cancel = true;
                        } else if ( k == 38 ) {
                            nextHistory();
                            cancel = true;
                        } else if ( k == 40 ) {
                            cancel = true;
                        } else if ( k == 37 || k == 39 ) {
                            cancel = true;
                        }
                    }

                    if ( cancel ) {
                        ev.preventDefault();
                        ev.stopPropagation();
                        return false;
                    }


                    if ( k == 8 ) {
                        if ( _buffer.length ) {
                            _buffer.pop();
                            ev.preventDefault();
                            $output.value = $output.value.substr(0, $output.value.length - 1);
                        } else {
                            ev.preventDefault();
                            return false;
                        }
                    }

                    return true;
                };

                $output.onkeypress = function(ev) {

                    ev.preventDefault();
                    if ( !_inited ) {
                        return false;
                    }

                    var k = ev.which || ev.keyCode;

                    if ( k == 13 ) {
                        var cmd = _buffer.join('').replace(/\s+/, ' ');
                        _buffer = [];
                        command(cmd);
                    } else {
                        if ( !_locked ) {
                            var kc = String.fromCharCode(k);
                            _buffer.push(kc);
                            _ibuffer.push(kc);
                        }
                    }

                    return true;
                };
        */

        $output.onfocus = function() {
            update();
        };

        $output.onblur = function() {
            update();
        };

        window.onfocus = function() {
            update();
            document.getElementById("textField").focus();
        };

        function onOpen() {
            print("Connected to " + wsUri+"\n");
        }
        function onMessage(evt) {
            var msg = JSON.parse(evt.data);
            switch(msg.type) {
                case "chat":
                    print (msg.message+"\n");
                    break;
                case "user":
                    print (msg.message+"\n");
                    break;
                case "system":
                    print ("System:"+msg.message+"\n");
                    break;

            }
        }

        _websocket.onmessage = function(evt) { onMessage(evt) };
        _websocket.onopen = function(evt) { onOpen(evt) };

        print("Welcome to Chat.\nType 'help' for help.\n");

    };

})();