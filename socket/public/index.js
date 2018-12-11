var socket = io();



//Initialize variables
var submitBtn = document.getElementById('submitBtn');
var typeInput = document.getElementById('typeInput');

var chatPage = document.querySelector('.chatPage');
var loginPage = document.querySelector('.loginPage');
var usernameInput = document.getElementsByClassName('usernameInput')[0];

//Prompt for setting a username
var connected = false;
var username;
var typing = false;
var lastTypingTime;


//click the submit
submitBtn.addEventListener('click', e => {
    e.preventDefault();
    addChatMessage({
        username: username,
        message: typeInput.value.trim(),
        align: 'right'
    });
}, false);

//log
const log = (msg, obj) => {
    if (!obj) obj = {};
    const li = document.createElement('li');
    console.log(msg, obj);
    li.innerHTML = msg;
    if (obj.log) {
        li.classList.add('log');
    } else {
        li.classList.add('chat');
        if (obj.align == 'right') {
            li.classList.add('mySelf')
        }
    }
    document.getElementById('messages').appendChild(li);
};

//added participants number message
const addParticipantsMessage = data => {
    let message = '';
    if (data.numUsers == 1) {
        message += "There's 1 participant."
    } else {
        message += "There are " + data.numUsers + ' participants.';
    }
    log(message, {
        log: true
    });
};

//sendMessage
const sendMessage = (message, isSelf) => {
    console.log('msg', message, connected);
    if (connected && message) {
        typeInput.value = '';
        //log into current client chat body
        addChatMessage({
            username: username,
            message: message,
            align: isSelf ? 'right' : ''
        });
        //log into other client char body
        socket.emit('new message', message)
    }
}

//set the client's username
const setUsername = () => {
    username = usernameInput.value.trim();
    if (username) {
        //login
        loginPage.style.display = 'none';
        chatPage.style.display = 'block';
        socket.emit('add user', usernameInput.value);
        typeInput.focus();
    }
};

//addChatMessage
const addChatMessage = data => {
    console.log('addChat', data);
    if (data.username && data.message) {
        if (data.align == 'right') {
            log(
                '<span class="chatText">' + data.message + '</span>'
                + '  ' +
                '<span class="chatUsername">' + data.username + '</span>',
                {
                    align: 'right'
                }
            );
        } else {
            log(
                '<span class="chatUsername">' + data.username + '</span>'
                + '  ' +
                '<span class="chatText">' + data.message + '</span>',
                {
                    align: ''
                }
            );
        }
    }
};

//keyboard event

window.onkeydown = e => {
    //auto-focus the current input when a key is typed
    if (!(e.ctrlKey || e.altKey || e.metaKey)) {
        usernameInput.focus();
        typeInput.focus();
    }
    if (e.keyCode == 13) {
        if (username) {
            //chat room
            loginPage.style.display = 'none';
            chatPage.style.display = 'block';
            sendMessage(typeInput.value.trim(), 'mySelf');
            typing = false;
        } else {
            setUsername();
        }
    }

};

//store the user data to localStorage

const recordUserData = data => {
    let userData = JSON.parse(localStorage.getItem('chatUser')) || [];
    console.log(userData, data.username);
    let obj = {
        username: data.username,
        userId: data.userId
    };
    if (_.findIndex(userData, o => {
        return o.username = data.username;
    }) > -1) {
        console.log('already joined');
    } else {
        userData = _.concat(userData, obj);
    }
    localStorage.setItem('chatUser', JSON.stringify(userData));
};

//socket events

//whether the server emits "login", log it in chart body
socket.on('login', data => {
    connected = true;
    var message = 'welcome to socket.io chart room ';
    log(message, {
        log: true,// prompt
    });
    recordUserData(data);
    addParticipantsMessage(data);
});

//whether the server emits "new message", log it in chart body
socket.on('new message', data => {
    addChatMessage(data);
});

//whether the server emits "user joined", log it in chart body
socket.on('user joined', data => {
    log(
        '<b>' + data.username + '</b> joined',
        {
            log: true,// prompt
        }
    );
    addParticipantsMessage(data);
});

//whether the server emits "user left", log it in chart body
socket.on('user left', data => {
    log(
        '<b>' + data.username + '</b> left',
        {
            log: true,// prompt
        }
    );
    const userData = JSON.parse(localStorage.getItem('chatUser')) || [];
    const left_index = _.findIndex(userData, o => { return o.username = data.username });
    userData.splice(left_index, 1);
    localStorage.setItem('chatUser', JSON.stringify(userData));
    addParticipantsMessage(data);
});


socket.on('reconnect', () => {
    console.log('you have been reconnected');
});