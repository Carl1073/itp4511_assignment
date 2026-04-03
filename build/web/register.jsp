<%-- Document : register Created on : 2026/4/3, 上午 05:07:31 Author : slt8ky --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>JSP Page</title>
            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
            <script>
                window.addEventListener('load', () => {
                    $('form').on('submit', async (e) => {
                        e.preventDefault();

                        if (!validatePassword() || !(await validateUsername())) return;

                        const { data } = await axios.post('main', new URLSearchParams({
                            action: 'register',
                            username: $('[name="username"]').val(),
                            password: $('[name="password"]').val(),
                            first_name: $('[name="first_name"]').val(),
                            last_name: $('[name="last_name"]').val(),
                            email: $('[name="email"]').val(),
                            gender: $('[name="gender"]').val()
                        }));

                        console.log(data);

                        if (data.success) {
                            window.location.href = 'home.jsp';
                        }
                    });

                    function validatePassword() {
                        const password = $('[name="password"]').val();
                        const passwordConfirm = $('[name="password_confirm"]').val();
                        const $e_password_confirm = $('.e_password_confirm');

                        if (!password.trim().length || !passwordConfirm.trim().length) {
                            $e_password_confirm.text('');
                            return false;
                        }

                        if (password !== passwordConfirm) {
                            $e_password_confirm.css('color', 'red').text('Password not match');
                            return false;
                        } else {
                            $e_password_confirm.css('color', 'green').text('Password matched');
                            return true;
                        }
                    }

                    async function validateUsername() {
                        const username = $('[name="username"]').val();
                        const $e_username = $('.e_username');
                        const { data } = await axios.post('main', new URLSearchParams({
                            action: 'checkusername',
                            username: username
                        }));

                        if (!username.trim().length) {
                            $e_username.text('');
                            return false;
                        }

                        if (data.exists) {
                            $e_username.css('color', 'red').text('Username already taken!');
                            return false;
                        } else {
                            $e_username.css('color', 'green').text('Username available');
                            return true;
                        }
                    }

                    $('[name="username"]').keyup(validateUsername);
                    $('[name="password"], [name="password_confirm"]').keyup(validatePassword);
                });
            </script>
        </head>

        <body>
            <form action="main" method="post">
                <input type="hidden" name="action" value="register">
                <div>
                    <div>
                        <div>Username</div>
                        <input type="text" name="username" required>
                        <span class="e_username"></span>
                    </div>
                    <div>
                        <div>Password</div>
                        <input type="password" name="password" required>
                    </div>
                    <div>
                        <div>Password Confirmation</div>
                        <input type="password" name="password_confirm" required>
                        <span class="e_password_confirm"></span>
                    </div>
                    <div>
                        <div>First name</div>
                        <input type="text" name="first_name" required>
                    </div>
                    <div>
                        <div>Last name</div>
                        <input type="text" name="last_name" required>
                    </div>
                    <div>
                        <div>Email</div>
                        <input type="email" name="email" required>
                    </div>
                    <div>
                        <div>Gender</div>
                        <select name="gender" required>
                            <option value="" selected disabled>-- Select Gender --</option>
                            <option value="male">male</option>
                            <option value="female">female</option>
                        </select>
                    </div>
                </div>
                <input type="submit" value="Create Account">
                <a href="login.jsp">login</a>
            </form>
        </body>

        </html>