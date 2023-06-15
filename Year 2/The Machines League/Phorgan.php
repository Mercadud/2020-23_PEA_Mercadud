<?php
const LOG_LOC = './data/log/';
function p_log(string $event) {
    $now = date('[d/m/Y H:i:s] ');
    file_put_contents(
        LOG_LOC .
        date('d_m_Y') .
        '.log',
        $now .
        $event .
        "\n",
        FILE_APPEND);
}

function p_u_log(string $event) {
    require_once(PLAYER_FUNCTIONS);
    $now = date('[d/m/Y H:i:s] ');
    $user = get_player_info($_SESSION['id'] ?? '');
    if ($user) {
        file_put_contents(
            LOG_LOC .
            date('d_m_Y') . '.log',
            $now .
            '-' .
            $user['fname'] .
            ' ' .
            $user['lname'] .
            ' (' .
            $user['id'] .
            ')' .
            '-'.
            $event .
            "\n",
            FILE_APPEND
        );
    }
    else {
        p_log($event);
    }
}
?>