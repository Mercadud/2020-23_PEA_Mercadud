<?php 
$e_old_pass = null;
$e_new_pass = null;
$e_con_pass = null;


function display_form($id) {
    global $e_old_pass, $e_new_pass, $e_con_pass;
?>
<div id="wrapper">
<form action="<?php echo HOME_PAGE ?>" method="post">
    <input type="hidden" name="csrf" value="<?php echo $_SESSION['csrf']?>">
    <input type="hidden" name="og_id" value="<?php echo $id ?>">
    <div>
        <label for="oldPass">Old Password:</label>
        <input type="password" name="oldPass" id="oldPass">
        <?php echo $e_old_pass ? "<p class='error'>$e_old_pass</p>" : '' ?>
    </div>
    <div>
        <label for="newPass">New Password</label>
        <input type="password" name="newPass" id="newPass">
        <?php echo $e_new_pass ? "<p class='error'>$e_new_pass</p>" : '' ?>
    </div>
    <div>
        <label for="conPass">Confirm Password</label>
        <input type="password" name="conPass" id="conPass">
        <?php echo $e_con_pass ? "<p class='error'>$e_con_pass</p>" : '' ?>
    </div>
    <div id="formBtns">
        <input type="submit" value="Edit Password" name="EditPass">
    </div>
</form>
</div>
<?php
}
?>

