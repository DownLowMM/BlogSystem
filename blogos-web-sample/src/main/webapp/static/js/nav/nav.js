function logout(bloggerId, bloggerName) {

    ajax('/blogger/' + bloggerId + '/logout', null, true, 'post',
        function (result) {
            if (result.code === 0) {
                location.href = '/' + bloggerName + '/archives';
            } else {
                toast(result.msg, 2000);
            }
        });

    // $.post(
    //     '/blogger/' + bloggerId + '/logout',
    //     function (result) {
    //         if (result.code === 0) {
    //             location.href = '/' + bloggerName + '/archives';
    //         } else {
    //             toast(result.msg, 2000);
    //         }
    //     }
    // );
}

function gotoRegister() {
    window.location = '/login';
}