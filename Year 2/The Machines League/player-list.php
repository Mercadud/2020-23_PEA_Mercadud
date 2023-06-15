<div id="listWrapper">
    <?php
    $lines = file( DATA_LOC );
    if ( !$lines ) {
    ?>
        <h1>No Players Have Been Added!</h1>
        <?php
    } else {

        foreach ( $lines as $key => $value ) {
            $value = explode( EXPLODER, $value );
            $fname = $value[0];
            $lname = $value[1];
            $email = $value[2];
            $city = $value[3];
            $country = $value[4];
            $pro = $value[5];
            $id = $value[6];
        ?>
            <div class="player">
                <div>
                    <h1 <?php echo $pro == 'yes' ? 'class="pro"' : '' ?>><?php echo "$value[0] $value[1]" ?></h1>
                    <img class="pf" src="<?php
                                            $result = ASSETS_LOC . 'soccarBoll.png';
                                            if ( file_exists( IMGDATA_LOC . $id . '.png' ) ) {
                                                $result = IMGDATA_LOC . $id . '.png';
                                            } elseif ( file_exists( IMGDATA_LOC . $id . '.gif' ) ) {
                                                $result = IMGDATA_LOC . $id . '.gif';
                                            } elseif ( file_exists( IMGDATA_LOC . $id . '.jpg' ) ) {
                                                $result = IMGDATA_LOC . $id . '.jpg';
                                            }
                                            echo $result;
                                            ?>" alt="Player Image" width="100">
                    <p>
                        <?php echo "$city ($country)" ?>
                    </p>
                    <a href=<?php echo "mailto:$email" ?>>
                        <?php echo $email ?>
                    </a>

                    <a class="edit" href=<?php echo HOME_PAGE . "?edit=$id" ?>><img src="./assets/pencil.png" alt="edit" width="30"></a>
                        <?php
                                            if ($id != $_SESSION['id']) {  
                        ?>
                    <a class="delete" href=<?php echo HOME_PAGE . "?delete=$id" ?>><img src="./assets/user-delete.png" alt="delete" width="30"></a>
                    <?php
                                            }
                    ?>
                </div>
            </div>
    <?php
        }
    }
    ?>
</div>
<form action="<?php echo HOME_PAGE?>" method="post" id="addPlayer">
    <input type="submit" value="Add Player" id="Add_Player" name="Add_Player">
</form>