<?php
function check_name(&$error, $name)
{
    if (empty($name)) {
        $error = "Please enter a Name";
    }
    elseif (preg_match("/[^a-z^A-Z-' ]/", $name)) {
        $error = "Can only contain letters, dash, apostrophe or space";
    }
}

function check_email(&$error, $email)
{
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $error = "Invalid email format : ex. Example@email.com";
    }
    else {
        $lines = file(DATA_LOC);
        foreach ($lines as $key => $value) {
            $data = explode(EXPLODER, $value);
            if (strtolower($email) == strtolower($data[2])) {
                $error = "Email already taken";
            }
        }
    }
}

function check_edit_email(&$error, $previousEmail, $email)
{
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $error = "Invalid email format : ex. Example@email.com";
    }
    else {
        $lines = file(DATA_LOC);
        foreach ($lines as $key => $value) {
            if ($email != $previousEmail) {
                $data = explode(EXPLODER, $value);
                if (strtolower($email) == strtolower($data[2])) {
                    $error = "Email already taken";
                }
            }
        }
    }
}

function check_general(&$error, $var)
{
    if (preg_match('/[~]/', $var)) {
        $error = '"~" is a reserved key';
    }
}
function exists(&$error, $value)
{
    if (empty($value)) {
        $error = "Please enter a response";
    }
}

function check_pass(&$error, $pass)
{
    // check number
    if (!preg_match("/[0-9]/", $pass)) {
        $error = "Missing number";
    }
    // check lowercase
    if (!preg_match("/[a-z]/", $pass)) {
        $error = "Missing lowercase letter";
    }
    // check uppercase
    if (!preg_match("/[A-Z]/", $pass)) {
        $error = "Missing uppercase letter";
    }
    // check spaces
    if (preg_match("/[ ]/", $pass)) {
        $error = "Cannot contain spaces";
    }
    // check symbol
    if (!preg_match("/[^a-zA-Z0-9]/", $pass)) {
        $error = "Missing symbol";
    }
    // check length
    if (!preg_match("/^.{8,16}$/", $pass)) {
        $error = "Length must be between 8 and 16 characters";
    }
    // check invalid symbols
    if (preg_match('/[~]/', $pass)) {
        $error = '"~" is a reserved key';
    }
}
function check_pass2(&$error, $pass1, $pass2)
{
    if ($pass1 !== $pass2) {
        $error = "Passwords are not the same";
    }
}

function check_img(&$error)
{
    $file = $_FILES['img'];
    if (file_exists($file['tmp_name'])) {
        $extension = strtolower(pathinfo(basename($file["name"]), PATHINFO_EXTENSION));
        if ($extension != 'png' && $extension != 'jpg' && $extension != 'gif') {
            $error = "invalid file type";
        }
    }
}

function check_country(&$error, $country)
{
    $valid = false;
    foreach (COUNTRY_LIST as $key => $value) {
        if ($country == $value) {
            $valid = true;
        }
    }
    if (!$valid) {
        $error = 'invalid country';
    }
}

function add_uploaded_image($name, $file)
{
    $extension = strtolower(pathinfo(basename($file['name']), PATHINFO_EXTENSION));
    move_uploaded_file($file['tmp_name'], IMGDATA_LOC . $name . '.' . $extension);
}

function check_change_pass(&$error, string $pass_entered, string $pass_saved) {
    if (!password_verify(trim($pass_entered), trim($pass_saved))) {
        $error = 'Passwords do not match';
    }
}

function add_line(string $line)
{
    $f = fopen(DATA_LOC, 'a');
    fwrite($f, $line);
    fclose($f);
}

function get_player_info(string $id)
{
    $lines = file(DATA_LOC);
    if ($id == 'master') {
        return array(
            'id' => 'no id',
            'fname' => 'Master',
            'lname' => 'Controller',
            'email' => '',
            'city' => '',
            'country' => '',
            'pro' => true,
            'pass' => '',
        );
    }
    foreach ($lines as $key => $player) {
        $player = explode(EXPLODER, $player);
        if ($id === $player[6]) {
            return array(
                'fname' => $player[0],
                'lname' => $player[1],
                'email' => $player[2],
                'city' => $player[3],
                'country' => $player[4],
                'pro' => $player[5] == 'yes',
                'id' => $player[6],
                'pass' => $player[7],
            );
        }
    }
    return false;
}

function delete_image(string $id)
{
    $photos = preg_grep("~$id.(jpg|gif|png)~", scandir(IMGDATA_LOC));
    $img = array_pop($photos);
    if ($img != "") {
        unlink(IMGDATA_LOC . $img);
    }
}

function go_home()
{
    header('location: ' . HOME_PAGE);
    exit();
}

function sign_in(string $email, string $pass)
{

    $possible_id = hash('whirlpool', $email);
    $player = get_player_info($possible_id);
    if ($player) {
        if ( password_verify(trim($pass), trim($player['pass']))) {
            return $possible_id;
        }
    }

    if (sizeof( file(DATA_LOC) ) < 1 && $email == 'master' && $pass == 'backdoor') {
        return 'master';
    }
    return false;
}

function edit_player($id, $player_info) {
    $lines = file(DATA_LOC);
    $players = [];
    foreach ($lines as $key => $player) {
        $player = explode(EXPLODER, $player);
        if ($player[6] == $id) {
            $players[] = array(
                $player_info['fname'],
                $player_info['lname'],
                $player_info['email'],
                $player_info['city'],
                $player_info['country'],
                $player_info['pro'],
                $player_info['id'],
                $player_info['pass']
            );
            $photos = preg_grep("~$id.(jpg|gif|png)~", scandir(IMGDATA_LOC));
            if ($photos) {
                $photos = array_pop($photos);
                $extension = strtolower(pathinfo(basename(IMGDATA_LOC . $photos), PATHINFO_EXTENSION));
                rename(IMGDATA_LOC . $photos, IMGDATA_LOC . $id . '.' . $extension);
            }
            if (isset($_FILES['img'])) {
                add_uploaded_image($id, $_FILES['img']);
            }
        } else {
            $players[] = $player;
        }
    }
    unlink(DATA_LOC);
    foreach ($players as $key => $player) {
        add_line(implode(EXPLODER, $player) );
    }
}