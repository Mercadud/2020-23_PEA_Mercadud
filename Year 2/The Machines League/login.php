<div id="wrapper">
    <form action="<?php echo HOME_PAGE?>" method="POST">
    <input type="hidden" name="csrf" value="<?php echo $_SESSION['csrf']?>">

    <h1>Login!</h1>
    <div>
        <p class="error"><?php echo $e_login ?? '' ?></p>
    </div>
        <div>
            <label for="email">Email:</label>
            <input type="text" name="email" id="email">
        </div>
        <div>
            <label for="password">Password:</label>
            <input type="password" name="password" id="password">
        </div>
        <div id="formBtns">
            <input type="submit" value="Sign In" id="sign_in" name="sign_in" >
        </div>
    </form>
</div>