package jp.diavolo.mapgenerator.original;

import java.util.Random;

class WaterRoomBuilder extends RandomRoomBuilder {
    public WaterRoomBuilder(Random rnd, int dungeonID) {
        super(rnd);
        int r;
        if(dungeonID == 1){
            r = rnd.nextInt(15);
        }else{
            r = rnd.nextInt(16);
        }

        switch(r){
        case 0:
            width = 9; height = 7;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.^^.^^.6"+
                "4.......6"+
                "4.^^.^^.6"+
                "4.......6"+
                "#2222222#"; break;
        case 1:
            width = 9; height = 8;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.^^....6"+
                "4.......6"+
                "4.......6"+
                "4....^^.6"+
                "4.......6"+
                "#2222222#"; break;
        case 2:
            width = 10; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#88888888#"+
                "4........6"+
                "4..^..^..6"+
                "4.^....^.6"+
                "4........6"+
                "4.^....^.6"+
                "4..^..^..6"+
                "4........6"+
                "#22222222#"; break;
        case 3:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#888888#"+
                "4......6"+
                "4..^.^.6"+
                "4.^.^..6"+
                "4......6"+
                "#222222#"; break;
        case 4:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = 2;
            map =
                "#888888#"+
                "4......6"+
                "4.^..^.6"+
                "4.^..^.6"+
                "4......6"+
                "#222222#"; break;
        case 5:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = 2;
            map =
                "#888888#"+
                "4......6"+
                "4.^^...6"+
                "4...^^.6"+
                "4......6"+
                "#222222#"; break;
        case 6:
            width = 10; height = 5;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#88888888#"+
                "4........6"+
                "4..^..^..6"+
                "4........6"+
                "#22222222#"; break;
        case 7:
            width = 9; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.^...^.6"+
                "4..^.^..6"+
                "4.^.^.^.6"+
                "4..^.^..6"+
                "4...^.^.6"+
                "4.......6"+
                "#2222222#"; break;
        case 8:
            width = 9; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.^.^...6"+
                "4..^.^..6"+
                "4.^.^.^.6"+
                "4..^....6"+
                "4.^..^^.6"+
                "4.......6"+
                "#2222222#"; break;
        case 9:
            width = 9; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888888#"+
                "4.......6"+
                "4...^.^.6"+
                "4..^.^..6"+
                "4.^.^.^.6"+
                "4..^.^..6"+
                "4.^...^.6"+
                "4.......6"+
                "#2222222#"; break;
        case 10:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#888888#"+
                "4......6"+
                "4....^.6"+
                "4......6"+
                "4......6"+
                "#222222#"; break;
        case 11:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#888888#"+
                "4......6"+
                "4..^...6"+
                "4......6"+
                "4......6"+
                "#222222#"; break;
        case 12:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#888888#"+
                "4......6"+
                "4...^^.6"+
                "4......6"+
                "4......6"+
                "#222222#"; break;
        case 13:
            width = 8; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#888888#"+
                "4......6"+
                "4.^.  .6"+
                "4.^....6"+
                "4......6"+
                "#222222#"; break;
        case 14:
            width = 6; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = 2;
            map =
                "##88##"+
                "#^..^#"+
                "4....6"+
                "4....6"+
                "#^..^#"+
                "##22##"; break;
        case 15:
            width = 6; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888#"+
                "4....6"+
                "4.^^.6"+
                "4.^^.6"+
                "4....6"+
                "#2222#"; break;
        }
    }
}