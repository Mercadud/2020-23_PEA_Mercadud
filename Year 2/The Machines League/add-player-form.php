    <?php
    $e_fname = '';
    $e_lname = '';
    $e_email = '';
    $e_city = '';
    $e_country = '';
    $e_pass = '';
    $e_pass2 = '';
    $e_img = '';
    $fname = '';
    $lname = '';
    $email = '';
    $city = '';
    $pass = '';
    $pass2 = '';
    $country = '';
    $pro = false;
    function display_form( $id )
    {
        global $e_fname, $e_lname, $e_email, $e_city, $e_pass, $e_pass2, $e_img, $fname, $lname, $email, $city, $pass, $pass2, $country, $pro;
        $is_edit = !empty( $id );
    ?>
        <div id="wrapper">
            <h1><?php echo $is_edit ? 'Edit Player' : 'Add Player' ?></h1>
            <form action="<?php echo HOME_PAGE?>" method="post" enctype="multipart/form-data">
                <input type="hidden" name="csrf" value="<?php echo $_SESSION['csrf']?>">
                <?php
                if ( !empty( $id ) ) { 
                    ?><input type="hidden" name="og_id" value="<?php echo $id ?>">
                    <?php 
                }
                    ?>
                <div>
                    <label for="fname">First Name:</label>
                    <input type="text" name="fname" id="fname" value="<?php echo $fname ?>">
                    <?php echo $e_fname ? "<p class='error'>$e_fname</p>" : '' ?>
                </div>
                <div>
                    <label for="lname">Last Name:</label>
                    <input type="text" name="lname" id="lname" value="<?php echo $lname ?>">
                    <?php echo $e_lname ? "<p class='error'>$e_lname</p>" : '' ?>
                </div>
                <div>
                    <label for="email">Email:</label>
                    <input type="text" name="email" id="email" value="<?php echo $email ?>">
                    <?php echo $e_email ? "<p class='error'>$e_email</p>" : '' ?>
                </div>
                <div>
                    <label for="city">City:</label>
                    <input type="text" name="city" id="city" value="<?php echo $city ?>">
                    <?php echo $e_city ? "<p class='error'>$e_city</p>" : '' ?>
                </div>
                <div>
                    <label for="country">Country:</label>
                    <select name="country" id="country">
                        <?php
                        foreach ( COUNTRY_LIST as $key => $value ) {
                        ?>
                            <option value="<?php echo $value ?>" <?php echo $value === $country ? 'selected="selected"' : '' ?>><?php echo $value ?></option>
                        <?php
                        }
                        ?>
                    </select>
                </div>
                <div>
                    <label for="pro">Professional:</label>
                    <input type="checkbox" id="pro" name="pro" value="pro" <?php echo $pro ? 'checked' : '' ?>>
                </div>
                <?php
                if ($is_edit) {
                    ?>
                    <div>
                        <label for="edit_pass">Edit Password:</label>
                        <a href="<?php echo HOME_PAGE . '?EditPass=' . $id ?>"><input type="button" value="Edit Password" name="EditPass" id="edit_pass"></a>
                    </div>
                    <?php
                }
                else {
                    ?>
                <div>
                    <label for="pass">Password:</label>
                    <input type="password" name="pass" id="pass" value="<?php echo $pass ?>">
                    <?php echo $e_pass ? "<p class='error'>$e_pass</p>" : '' ?>
                </div>
                <div>
                    <label for="pass2">Confirm Password:</label>
                    <input type="password" name="pass2" id="pass2" value="<?php echo $pass2 ?>">
                    <?php echo $e_pass2 ? "<p class='error'>$e_pass2</p>" : '' ?>
                </div>
                    <?php
                }
                ?>

                <?php
                if ( $is_edit && preg_grep( "~$id.(jpg|gif|png)~", scandir( IMGDATA_LOC ) ) ) {
                ?>
                    <a href=<?php echo HOME_PAGE . '?edit=' . $id . '&del=' . true ?>>Delete Image</a>
                <?php
                } else {
                ?>
                    <div>
                        <label for="img">Add an image:</label>
                        <input type="file" name="img" id="img">
                        <?php echo $e_img ? "<p class='error'>$e_pass</p>" : '' ?>
                    </div>
                <?php } ?>
                <br />
                <div id="formBtns">
                    <input type="submit" value=<?php echo $is_edit ? 'Edit Player' : 'Submit Player' ?> id="Submit_Player" name=<?php echo $is_edit ? 'Edit_Player' : 'Submit_Player' ?>>
                    <input type="submit" value="Go Back">
                </div>
            </form>
        </div>
    <?php } ?>