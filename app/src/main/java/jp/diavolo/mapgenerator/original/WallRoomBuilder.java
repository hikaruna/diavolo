package jp.diavolo.mapgenerator.original;

import java.util.Random;

class WallRoomBuilder extends RandomRoomBuilder {
    public WallRoomBuilder(Random rnd) {
        super(rnd);
        int r = rnd.nextInt(13);
        switch(r){
        case 0:
            width = 10; height = 5;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#88888888#"+
                "4........6"+
                "4...##...6"+
                "4........6"+
                "#22222222#";
            break;
        case 1:
            width = 9; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.#.#.#.6"+
                "4.......6"+
                "4.#.#.#.6"+
                "4.......6"+
                "4.#.#.#.6"+
                "4.......6"+
                "#2222222#"; break;
        case 2:
            width = 10; height = 5;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width -2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height -2;
            map =
                "#88888888#"+
                "4........6"+
                "4..#..#..6"+
                "4........6"+
                "#22222222#"; break;
        case 3:
            width = 9; height = 8;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = 2;
            map =
                "##8###8##"+
                "#.......#"+
                "#...#...#"+
                "4.......6"+
                "4.......6"+
                "#...#...#"+
                "#.......#"+
                "##2###2##"; break;
        case 4:
            width = 10; height = 8;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = 2;
            map =
                "####88####"+
                "#........#"+
                "#..#..#..#"+
                "4........6"+
                "4........6"+
                "#..#..#..#"+
                "#........#"+
                "####22####"; break;
        case 5:
            width = 10; height = 8;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = 2;
            map =
                "####88####"+
                "#........#"+
                "#........#"+
                "4...##...6"+
                "4...##...6"+
                "#........#"+
                "#........#"+
                "####22####"; break;
        case 6:
            width = 9; height = 7;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.#.#.#.6"+
                "4.......6"+
                "4.#.#.#.6"+
                "4.......6"+
                "#2222222#"; break;
        case 7:
            width = 7; height = 5;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#88888#"+
                "4.....6"+
                "4.#.#.6"+
                "4.....6"+
                "#22222#"; break;
        case 8:
            width = 7; height = 5;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#88888#"+
                "4.....6"+
                "4..#..6"+
                "4.....6"+
                "#22222#"; break;
        case 9:
            width = 7; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#88888#"+
                "4.....6"+
                "4.#...6"+
                "4...#.6"+
                "4.....6"+
                "#22222#"; break;
        case 10:
            width = 7; height = 6;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#88888#"+
                "4.....6"+
                "4...#.6"+
                "4.#...6"+
                "4.....6"+
                "#22222#"; break;
        case 11:
            width = 9; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888888#"+
                "4.......6"+
                "4.#.#...6"+
                "4.......6"+
                "4...##..6"+
                "4.#.....6"+
                "4...#.#.6"+
                "4.......6"+
                "#2222222#"; break;
        case 12:
            width = 9; height = 9;
            doorwayCount[DoorWay.UP] = doorwayCount[DoorWay.DOWN] = width - 2;
            doorwayCount[DoorWay.LEFT] = doorwayCount[DoorWay.RIGHT] = height - 2;
            map =
                "#8888888#"+
                "4.......6"+
                "4..#..#.6"+
                "4.......6"+
                "4.#.....6"+
                "4....##.6"+
                "4..#....6"+
                "4.......6"+
                "#2222222#"; break;
        }
    }
}