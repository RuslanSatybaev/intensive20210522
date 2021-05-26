$(function(){
        let initChat = function() {
    alert('YES')
        };

        let authUser = function() {
            let name = prompt('Введите имя пользователя:');
            $.post('/api/users', {'name': name}, function(response){
                if(response.result) {
                    initChat();
                } else {
                    alert('Что-то пошло не так :(');
                }
            });
        }

        let checkAuthStatus = function() {
           $.get('/api/auth', function(response){
                if(response.result) {
                    initChat();
                } else {
                    authUser();
                }
           });
        }

        checkAuthStatus();
    });