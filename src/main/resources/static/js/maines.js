$(function () {
    let initChat = function () {
        //    alert('YES')
        loadUsers();
    };

    let loadUsers = function () {
        // $.get('/api/users', function (response) {
        //     let users = response.users;
        //     let usersList = $('.users-list');
        //     for (let i in users) {
        //         let userItem = $('<div class="user-item"></div>');
        //         userItem.text(users[i].name);
        //         usersList.append(userItem);
        //     }

        // });
        let name = prompt('Введите имя пользователя:');
    };

    let authUser = function () {
        let name = prompt('Введите имя пользователя:');
        $.post('/api/users', { 'name': name }, function (response) {
            if (response.result) {
                initChat();
            } else {
                alert('Что-то пошло не так :(');
            }
        });
    }

    let checkAuthStatus = function () {
        $.get('/api/auth', function (response) {
            if (response.result) {
                initChat();
            } else {
                authUser();
            }
        });
    }

    checkAuthStatus();

    $('.send-message').on('click', function () {
        let message = $('.message-text').val();
        $.post('/api/messages', { 'text': message }, function (response) {
            if (response.result) {
                $('.message-text').val('');
            } else {
                alert('чтото пошло не так')
            }
        })
    })
});