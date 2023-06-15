<?php
declare(strict_types=1);
session_start();
ini_set('display_errors', '1');
header('Content-Security-Policy: frame-ancestors');
header('X-Content-Type-Options: nosniff');
const DATA_LOC = './data/players.txt';
const EXPLODER = '~';
const IMGDATA_LOC = './data/playerImages/';
const ASSETS_LOC = './assets/';
const COUNTRY_LIST = array(
    "Canada",
    "United States",
    "Argentina",
    "Brazil",
    "Columbia",
    "Ecuador",
    "Peru",
    "Poland"
);
const HOME_PAGE = './player-league.php';
const PLAYER_FUNCTIONS = './PlayerFunctions.php';
const ADD_PLAYER_FORM = './add-player-form.php';
const LOGIN_PAGE = './login.php';
const EDIT_PASSWORD = './edit-password.php';
error_reporting(E_ALL);
if (!file_exists(DATA_LOC)) {
    $f = fopen(DATA_LOC, "w");
    fclose($f);
}

if (!isset($_SESSION['signed_in'])) {
    $_SESSION['signed_in'] = false;
}

require_once('./PlayerFunctions.php');

$verified = false;
if (isset($_POST['csrf'])) {
    if ($_SESSION['csrf'] == $_POST['csrf']) {
        $verified = true;
    }
}
$_SESSION['csrf'] = rand(PHP_INT_MIN, PHP_INT_MAX);

require_once('./Phorgan.php');
?>

<head>
    <link rel="stylesheet" href="./styles/player-league.css">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MACHINE LEAGUE</title>
</head>

<body>
    <script src="./script/smoke_anime.js"></script>
    <?php 
        if ($_SESSION['signed_in']) {
            require_once(PLAYER_FUNCTIONS);
            // check if he is deleted
            $player = get_player_info($_SESSION['id']);
            if (!$player) {
                go_home();
            }
    ?>
    <header>

        <div class="PName">
        <h2><?php echo $player['fname'] . ' ' . $player['lname'] ?></h2>
        <p>Signed in on: <?php echo $_SESSION['time'] ?></p>
        </div>
        <div>
            <h1>The Machinesss Football League</h1>
        </div>
        <div>
        <?php
            ?>
            <form action="<?php echo HOME_PAGE ?>" method="POST">
            <input type="hidden" name="csrf" value="<?php echo $_SESSION['csrf']?>">
            <input type="submit" value="Sign Out" name="sign_out">
        </form>
        </div>
    </header>
    <?php 
    } else {
        ?>
        <header>
            <div></div>
            <h1>The Machinesss Football League</h1>
            <div></div>
        </header>
        <?php
    }
    
    ?>
    <div id="master">
        <?php
        if ($_SESSION['signed_in']) {
            // check if timeout
            if (!isset($_SESSION['timeout'])) {
                $_SESSION['timeout'] = time();
            }
            if ($_SESSION['timeout'] + 10 * 60 < time()) {
                require_once('./Phorgan.php');
                p_u_log('Timed Out');
                session_unset();
                session_destroy();
                go_home();
            }

            $_SESSION['timeout'] = time();
            require_once(PLAYER_FUNCTIONS);
            if (isset($_POST['sign_out'])) {
                if ($verified) {
                    p_u_log('logged out');
                    session_unset();
                }
                go_home();
            } else if (isset($_GET['delete'])) {
                if ($_SESSION['id'] != $_GET['delete']) {
                    $player_name = 'player not found';
                    $players = file(DATA_LOC);
                    $id = null;
                    $save = array();
                    foreach ($players as $key => $value) {
                        $ex = explode(EXPLODER, $value);
                        if ($ex[6] !== $_GET['delete']) {
                            array_push($save, $value);
                        } else {
                            $player_name = $ex[0] . ' ' . $ex[1] . ' has been deleted';
                            $id = $ex[6];
                        }
                    }
                    if ($id) {
                        delete_image($id);
                    }
                    $f = @fopen(DATA_LOC, "r+");
                    if ($f !== false) {
                        ftruncate($f, 0);
                        fclose($f);
                    }
                    foreach ($save as $key => $value) {
                        add_line($value);
                    }
                    p_u_log($player_name);
            ?>
                    <h1><?php echo $player_name ?></h1>
                    <a href="<?php echo HOME_PAGE ?>" class="goBack">Go back?</a>
                    <?php
                }
                else {
                    p_u_log("Tried deleting himself...");
                    ?>
                    <h1>Deleting yourself is NOT an option...</h1>
                    <a href="<?php echo HOME_PAGE ?>" class="goBack">Go back?</a>
                    <?php
                }
            } else {
                if (isset($_POST['Submit_Player'])) {
                    require_once(ADD_PLAYER_FORM);
                    $fname = htmlspecialchars($_POST['fname'] ?? '');
                    check_general($e_fname, $fname);
                    check_name($e_fname, $fname);
                    $lname = htmlspecialchars($_POST['lname'] ?? '');
                    check_general($e_lname, $lname);
                    check_name($e_lname, $lname);
                    $email = htmlspecialchars($_POST['email'] ?? '');
                    check_general($e_email, $email);
                    check_email($e_email, $email);
                    $city = htmlentities($_POST['city'] ?? '');
                    check_general($e_city, $city);
                    exists($e_city, $city);
                    $pass = trim(htmlspecialchars($_POST['pass'] ?? ''));
                    check_general($e_pass, $pass);
                    check_pass($e_pass, $pass);
                    $pass2 = trim(htmlspecialchars($_POST['pass2'] ?? ''));
                    check_general($e_pass, $pass);
                    check_pass2($e_pass2, $pass, $pass2);
                    $country = htmlspecialchars($_POST['country'] ?? '');
                    check_general($e_country, $country);
                    check_country($e_country, $country);
                    $pro = isset($_POST['pro']);
                    check_img($e_img);
                    if (!$verified) {
                        p_u_log('had a csrf moment while creating a player');
                        ?>
                        <script>alert('Invalid Token')</script>
                        <?php
                    }
                    if ($e_city || $e_email || $e_fname || $e_pass || $e_pass2 || $e_img || $e_country || !$verified) {
                        display_form('');
                    } else {
                        $id = hash('whirlpool', $email);
                        p_u_log("Added a player : $id");
                        $pass = password_hash($pass2, PASSWORD_DEFAULT);
                        add_line(
                            $fname
                                . EXPLODER
                                . $lname
                                . EXPLODER
                                . $email
                                . EXPLODER
                                . $city
                                . EXPLODER
                                . $country
                                . EXPLODER
                                . ($pro ? 'yes' : 'no')
                                . EXPLODER
                                . $id
                                . EXPLODER
                                . $pass
                                . "\n"
                        );
                        add_uploaded_image($id, $_FILES['img']);
                ?>
                        <h2><?php echo  $fname . ' ' . $lname ?> has been added</h2>
                        <a href="<?php echo HOME_PAGE ?>" class="goBack">Go Back</a>
                    <?php
                    }
                } elseif (isset($_POST['Add_Player'])) {
                    require_once(ADD_PLAYER_FORM);
                    display_form('');
                } elseif (isset($_GET['edit'])) {
                    require_once(ADD_PLAYER_FORM);
                    $og_id = $_GET['edit'];
                    if (isset($_GET['del'])) {
                        delete_image($og_id);
                    }
                    $player = get_player_info($og_id);
                    if (!$player) {
                        go_home();
                    }
                    $fname = $player['fname'];
                    $lname = $player['lname'];
                    $email = $player['email'];
                    $city = $player['city'];
                    $pass = $player['pass'];
                    $country = $player['country'];
                    $pro = isset($player['pro']);
                    display_form($og_id);
                } elseif (isset($_POST['Edit_Player'])) {
                    require_once(ADD_PLAYER_FORM);
                    $og_id = $_POST['og_id'] ?? '';
                    $og_info = get_player_info($og_id);
                    if (!$og_info) {
                        p_u_log('Tried Editing a player who doesn\'t exist');
                        go_home();
                    }
                    $fname = htmlspecialchars($_POST['fname'] ?? '');
                    check_general($e_fname, $fname);
                    check_name($e_fname, $fname);
                    $lname = htmlspecialchars($_POST['lname'] ?? '');
                    check_general($e_lname, $lname);
                    check_name($e_lname, $lname);
                    $email = htmlspecialchars($_POST['email'] ?? '');
                    check_general($e_email, $email);
                    check_edit_email($e_email, $og_info['email'], $email);
                    $city = htmlspecialchars($_POST['city'] ?? '');
                    check_general($e_city, $city);
                    exists($e_city, $city);
                    $country = htmlspecialchars($_POST['country'] ?? '');
                    check_general($e_country, $country);
                    check_country($e_country, $country);
                    $pro = isset($_POST['pro']);
                    check_img($e_img);
                    if (!$verified) {
                        p_u_log('had a csrf moment while editing a player');
                        ?>
                        <script>alert('Invalid Token')</script>
                        <?php
                    }
                    if ($e_city || $e_email || $e_fname || $e_img || $e_country || !$verified) {
                        display_form($og_id);
                    } else {
                        $og_info['fname'] = $fname;
                        $og_info['lname'] = $lname;
                        $og_info['email'] = $email;
                        $og_info['city'] = $city;
                        $og_info['country'] = $country;
                        $og_info['pro'] = $pro ? 'yes' : 'no' ;
                        $og_info['id'] = hash('whirlpool', $email);
                        p_u_log("edited : $fname $lname");
                        edit_player($og_id, $og_info);
                    ?>
                        <h1>successfully edited <?php echo $fname . $lname ?></h1>
                        <a href="<?php echo HOME_PAGE ?>" class="goBack">Go back?</a>
        <?php
                    }
                }
                else if (isset($_GET['EditPass'])) {
                    $player = get_player_info($_GET['EditPass']);
                    if (!$player) {
                        go_home();
                    }
                    require_once(EDIT_PASSWORD);
                    display_form($_GET['EditPass']);
                }
                else if (isset($_POST['EditPass'])) {
                    require_once(EDIT_PASSWORD);
                    $player = get_player_info($_POST['og_id']);
                    if (!$player) {
                        go_home();
                    }
                    $old_pass = $_POST['oldPass'] ?? '';
                    check_change_pass($e_old_pass, $old_pass, trim($player['pass']));
                    $new_pass = htmlspecialchars( $_POST['newPass'] ?? '' );
                    check_pass($e_new_pass, $new_pass);
                    check_general($e_new_pass, $new_pass);
                    $con_pass = htmlspecialchars( $_POST['conPass'] ?? '' );
                    check_pass2($e_con_pass, $new_pass, $con_pass);
                    if (!$verified) {
                        p_u_log('had a csrf moment while editing their password');
                        ?>
                        <script>alert('Invalid Token')</script>
                        <?php
                    }
                    if ($e_old_pass || $e_new_pass || $e_con_pass || !$verified) {
                        display_form($_POST['og_id']);
                    } else {
                        p_u_log('Edited their password succesfully');
                        $player['pass'] = password_hash($new_pass, PASSWORD_DEFAULT);
                        edit_player($_POST['og_id'], $player);
                        ?>
                        <h1>successfully edited the password of: <?php echo $player['fname'] . $player['lname'] ?></h1>
                        <a href="<?php echo HOME_PAGE ?>" class="goBack">Go back?</a>
                        <?php
                    }
                }
                else {
                    require_once('./player-list.php');
                }
            }
        } else {
            require_once(PLAYER_FUNCTIONS);
            $e_login = null;
            if (isset($_POST['sign_in'])) {
                $possible_id = sign_in(
                    htmlspecialchars( $_POST['email'] ?? '' ),
                    trim(htmlspecialchars( $_POST['password'] ?? '' ) ));
                if (!$verified) {
                        p_log("invalid csrf moment while signing in");
                        ?>
                        <script>alert('Invalid Token')</script>
                        <?php
                        $possible_id = false;
                    }
                if ($possible_id) {
                    $_SESSION['id'] = $possible_id;
                    $_SESSION['time'] = date('l jS \of F Y h:i:s A');
                    $_SESSION['signed_in'] = true;
                    p_u_log('Signed in');
                    go_home();
                }
                else {
                    $e_login = 'Invalid email and/or password';
                }
            }
            require_once(LOGIN_PAGE);
        }
        ?>
    </div>
</body>