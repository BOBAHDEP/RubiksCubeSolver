package Cube;

import java.util.Random;

public class Cube {

    private int [][][] cube = new int[3][3][6];

    private int numberOfSteps;

    Cube(int [][][] cube){
        this.cube = cube;
    }

    Cube(boolean isSolved){
        numberOfSteps = 0;
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
            for (int k = 0; k < 6; k++){
                cube[i][j][k] = k;
            }
            }
        }
        if (!isSolved){
            Random rand;
            int number;
            for (int i = 0; i < 100; i++){
                rand = new Random();
                number = rand.nextInt(3);
                horizontalMoveRight(number);
                rand = new Random();
                number = rand.nextInt(3);
                verticalMoveUP(number);
                moveSideUP();
                rotateMiddlePositiveAxis();
                rotateBottomPositiveAxis();
            }
        }
    }

    public void cubeOut(){
        for (int i = 0; i < 3; i++){
            System.out.println("      " + cube[0][i][4] + " " + cube[1][i][4] + " " + cube[2][i][4] + "      ");
        }
        System.out.println("      -----     ");
        for (int i = 0; i < 3; i++){
            System.out.println(cube[0][i][1] + " " + cube[1][i][1] + " " + cube[2][i][1] + "|" + cube[0][i][2] + " " + cube[1][i][2] + " " + cube[2][i][2] + "|" + cube[0][i][3] + " " + cube[1][i][3] + " " + cube[2][i][3] + "|" + cube[2][i][5] + " " + cube[1][i][5] + " " + cube[0][i][5]);
        }
        System.out.println("      -----     ");
        for (int i = 0; i < 3; i++){
            System.out.println("      " + cube[0][i][0] + " " + cube[1][i][0] + " " + cube[2][i][0] + "      ");
        }
        System.out.println("      -----     ");
    }

    private boolean checkWinOneSide(int sideNumber){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                if (cube[i][j][sideNumber] != cube[0][0][sideNumber]){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkWin(){
        for (int i = 0; i < 6; i++){
            if (!checkWinOneSide(i)){
                return false;
            }
        }
        return true;
    }

    private boolean checkWinMiddleSide(){
        return cube[0][1][0] == cube[1][1][0] && cube[2][1][0] == cube[1][1][0] &&
                cube[1][0][1] == cube[1][1][1] && cube[1][2][1] == cube[1][1][1] &&
                cube[0][1][4] == cube[1][1][4] && cube[2][1][4] == cube[1][1][4] &&
                cube[1][0][3] == cube[1][1][3] && cube[1][2][3] == cube[1][1][3];
    }

    private boolean checkTurnedUPWinMiddleSide(){
        return cube[0][1][2] == cube[1][1][2] && cube[2][1][2] == cube[1][1][2] &&
                cube[0][1][3] == cube[1][1][3] && cube[2][1][3] == cube[1][1][3] &&
                cube[0][1][1] == cube[1][1][1] && cube[2][1][1] == cube[1][1][1] &&
                cube[0][1][5] == cube[1][1][5] && cube[2][1][5] == cube[1][1][5];
    }

    private boolean checkThreeNumbers(int[] numbers){
        for (int i = 0; i < numbers.length; i++){
            if (numbers[i]%10 == numbers[i]/100 || numbers[i]%10 == numbers[i]/10%10 || numbers[i]/100 == numbers[i]/10%10){
                return false;
            }
            for (int j = 0; j < numbers.length; j++){
                if ((i != j) && (numbers[i] == numbers[j] ||
                                 numbers[i]%10*100+numbers[i]/10%10*10+numbers[i]/100 == numbers[j]%10*100+numbers[j]/10%10*10+numbers[j]/100 ||
                                 numbers[i]/10%10*100+numbers[i]/100*10+numbers[i]%10 == numbers[j]/10%10*100+numbers[j]/100*10+numbers[j]%10 ||
                                 numbers[i]/100*100+numbers[i]%10*10+numbers[i]/10%10 == numbers[j]/100*100+numbers[j]%10*10+numbers[j]/10%10 ||
                                 numbers[i]%10*100+numbers[i]/100*10+numbers[i]/10%10 == numbers[j]%10*100+numbers[j]/100*10+numbers[j]/10%10 ||
                                 numbers[i]/10%10*100+numbers[i]%10*10+numbers[i]/100 == numbers[j]/10%10*100+numbers[j]%10*10+numbers[j]/100
                )){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkRightPositionOfCubeParts(){

        int [] linePartsOfCube = initializationTwoParticles();

        int []  cornerPartsOfCube = initializationCornerParticles();

        for (int i = 0; i < 6; i++){      // check central parts of cube
            for (int j = 0; j < 6; j++){
                if (cube[1][1][i] == cube[1][1][j] && i != j){
                    return false;
                }
            }
        }

        for (int i = 0; i < 12; i++){      // check double parts of cube
            for (int j = 0; j < 12; j++){                                    // 12 == 12 || 12 = 21
                if ((linePartsOfCube[i] == linePartsOfCube[j]  || linePartsOfCube[i] == (linePartsOfCube[j]%10*10+linePartsOfCube[j]/10) || linePartsOfCube[i]/10 == linePartsOfCube[i]%10) && i != j){
                    return false;
                }
            }
        }
        return checkThreeNumbers(cornerPartsOfCube);  // check corner parts of cube
    }

    private void changeArrayToPositive(int sideNumber){
        int color;
        color = cube[0][2][sideNumber]*100 + cube[1][2][sideNumber]*10 + cube[2][2][sideNumber];
        for (int i = 0; i < 3; i++){
            cube[i][2][sideNumber] = cube[0][i][sideNumber];
            if (i != 2){
                cube[0][i][sideNumber] = cube[2-i][0][sideNumber];
                cube[2-i][0][sideNumber] = cube[2][2-i][sideNumber];
            }
        }
        cube[2][0][sideNumber] = color%10;
        cube[2][1][sideNumber] = color/10%10;
        cube[2][2][sideNumber] = color/100;
    }

    private void changeArrayToNegative(int sideNumber){
        int color;
        color = cube[0][2][sideNumber]*100 + cube[1][2][sideNumber]*10 + cube[2][2][sideNumber];
        for (int i = 0; i < 3; i++){
            cube[i][2][sideNumber] = cube[2][2-i][sideNumber];
        }
        for (int i = 0; i < 3; i++){
            cube[2][2-i][sideNumber] = cube[2-i][0][sideNumber];
        }
        for (int i = 0; i < 3; i++){
            cube[2-i][0][sideNumber] = cube[0][i][sideNumber];
        }

        cube[0][2][sideNumber] = color%10;
        cube[0][1][sideNumber] = color/10%10;
        cube[0][0][sideNumber] = color/100;
    }

    private void horizontalMoveRight(int lineNumber, boolean isPrinted){      //horizontal move of line to the right
        int color;
        for (int i = 0; i < 3; i++){
            color = cube[i][lineNumber][2];
            cube[i][lineNumber][2] = cube[i][lineNumber][1];
            cube[i][lineNumber][1] = cube[2-i][lineNumber][5];
            cube[2-i][lineNumber][5] = cube[i][lineNumber][3];
            cube[i][lineNumber][3] = color;
        }
        if (lineNumber == 0){
            changeArrayToPositive(4);
        }
        if (lineNumber == 2){
            changeArrayToNegative(0);
        }
        if (isPrinted){
            printStepSolve("horizontalMoveRight on " + lineNumber);
        }
    }

    private void horizontalMoveRight(int lineNumber){
        horizontalMoveRight(lineNumber, true);
    }

    private void horizontalMoveReverse(int lineNumber, boolean isPrinted){
        for (int i = 0; i < 2; i++){
            horizontalMoveRight(lineNumber, false);
        }
        if (isPrinted){
            printStepSolve("horizontalMoveReverse on " + lineNumber);
        }
    }

    private void horizontalMoveReverse(int lineNumber){
        horizontalMoveReverse(lineNumber, true);
    }

    private void moveSideToTheRight(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            horizontalMoveRight(i, false);
        }
        if (isPrinted){
            printStepSolve("moveSideToTheRight");
        }
    }

    private void moveSideToTheRight(){
        moveSideToTheRight(true);
    }

    private void horizontalMoveLeft(int lineNumber, boolean isPrinted){
        for (int i = 0; i < 3; i++){
            horizontalMoveRight(lineNumber, false);
        }
        if (isPrinted){
            printStepSolve("horizontalMoveLeft on " + lineNumber);
        }
    }

    private void horizontalMoveLeft(int lineNumber){
        horizontalMoveLeft(lineNumber, true);
    }

    private void moveSideToTheLeft(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            horizontalMoveLeft(i, false);
        }
        if (isPrinted){
            printStepSolve("moveSideToTheLeft");
        }
    }

    private void moveSideToTheLeft(){
        moveSideToTheLeft(true);
    }

    private void verticalMoveDown(int lineNumber, boolean idPrinted){
        int color;
        for (int i = 0; i < 3; i++){
            color = cube[lineNumber][i][2];
            cube[lineNumber][i][2] = cube[lineNumber][i][4];
            cube[lineNumber][i][4] = cube[lineNumber][2-i][5];
            cube[lineNumber][2-i][5] = cube[lineNumber][i][0];
            cube[lineNumber][i][0] = color;
        }
        if (lineNumber == 0){
            changeArrayToNegative(1);
        }
        if (lineNumber == 2){
            changeArrayToPositive(3);
        }
        if (idPrinted){
            printStepSolve("verticalMoveDown on " + lineNumber);
        }
    }

    private void verticalMoveDown(int lineNumber){
        verticalMoveDown(lineNumber, true);
    }

    private void verticalMoveUP(int lineNumber, boolean isPrinted){
        for (int i = 0; i < 3; i++){
            verticalMoveDown(lineNumber, false);
        }
        if (isPrinted){
            printStepSolve("verticalMoveUP");
        }
    }

    private void verticalMoveUP(int lineNumber){
        verticalMoveUP(lineNumber, true);
    }

    private void verticalMoveReverse(int lineNumber, boolean isPrinted){
        for (int i = 0; i < 2; i++){
            verticalMoveDown(lineNumber, false);
        }
        if (isPrinted){
            printStepSolve("verticalMoveReverse on " + lineNumber);
        }
    }

    private void verticalMoveReverse(int lineNumber){
        verticalMoveReverse(lineNumber, true);
    }

    private void moveSideUP(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            verticalMoveUP(i, false);
        }
        if (isPrinted){
            printStepSolve("moveSideUP");
        }
    }

    private void moveSideUP(){
        moveSideUP(true);
    }

    private void moveSideDown(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            verticalMoveDown(i, false);
        }
        if (isPrinted){
            printStepSolve("moveSideDown");
        }
    }

    private void moveSideDown(){
        moveSideDown(true);
    }

    private void rotateBottomPositiveAxis(boolean isPrinted){
        moveSideToTheLeft(false);
        verticalMoveDown(0,false);
        moveSideToTheRight(false);
        if (isPrinted){
            printStepSolve("rotateBottomPositiveAxis");
        }
    }

    private void rotateBottomPositiveAxis(){
        rotateBottomPositiveAxis(true);
    }

    private void rotateBottomNegativeAxis(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            rotateBottomPositiveAxis(false);
        }
        if (isPrinted){
            printStepSolve("rotateBottomNegativeAxis");
        }
    }

    private void rotateBottomNegativeAxis(){
        rotateBottomNegativeAxis(true);
    }

    private void rotateDownSidePositiveAxis(boolean  isPrinted){
        moveSideDown(false);
        moveSideDown(false);
        rotateBottomNegativeAxis(false);
        moveSideDown(false);
        moveSideDown(false);
        if (isPrinted){
            printStepSolve("rotateDownSidePositiveAxis");
        }
    }

    private void rotateDownSidePositiveAxis(){
        rotateDownSidePositiveAxis(true);
    }

    private void rotateDownSideNegativeAxis(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            rotateDownSidePositiveAxis(false);
        }
        if (isPrinted){
            printStepSolve("rotateDownSideNegativeAxis");
        }
    }

    private void rotateDownSideNegativeAxis(){
        rotateDownSideNegativeAxis(true);
    }

    private void rotateMiddlePositiveAxis(boolean isPrinted){
        moveSideUP(false);
        horizontalMoveLeft(1, false);
        moveSideDown(false);
        if (isPrinted){
            printStepSolve("rotateMiddlePositiveAxis");
        }
    }

    private void rotateMiddlePositiveAxis(){
        rotateMiddlePositiveAxis(true);
    }

    private void rotateMiddleNegativeAxis(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            rotateMiddlePositiveAxis(false);
        }
        if (isPrinted){
            printStepSolve("rotateMiddleNegativeAxis");
        }
    }

    private void rotateMiddleNegativeAxis(){
        rotateMiddleNegativeAxis(true);
    }

    private void rotateAllPositiveAxis(boolean isPrinted){
        rotateBottomPositiveAxis(false);
        rotateMiddlePositiveAxis(false);
        rotateDownSidePositiveAxis(false);
        if (isPrinted){
            printStepSolve("rotateAllPositiveAxis");
        }
    }

    private void rotateAllPositiveAxis(){
        rotateAllPositiveAxis(true);
    }

    private void rotateAllNegativeSide(boolean isPrinted){
        for (int i = 0; i < 3; i++){
            rotateAllPositiveAxis(false);
        }
        if (isPrinted){
            printStepSolve("rotateAllNegativeSide");
        }
    }

    private void rotateAllNegativeSide(){
        rotateAllNegativeSide(true);
    }

    private int [] initializationTwoParticles(){
        int [] array = new int[12];
        array[0] = cube[2][1][1]*10 + cube[0][1][2];                     //      000
        array[1] = cube[1][2][4]*10 + cube[1][0][2];                     //      000
        array[2] = cube[2][1][2]*10 + cube[0][1][3];                     //      010
        array[3] = cube[1][0][0]*10 + cube[1][2][2];                     //      ---
        array[4] = cube[0][1][5]*10 + cube[0][1][1];                     //  000|010|000|000
        array[5] = cube[1][0][5]*10 + cube[1][0][4];                     //  00o|o00|000|000
        array[6] = cube[2][1][3]*10 + cube[2][1][5];                     //  000|000|000|000
        array[7] = cube[1][2][5]*10 + cube[1][2][0];                     //      ---
        array[8] = cube[1][2][1]*10 + cube[0][1][0];                     //      000
        array[9] = cube[1][0][1]*10 + cube[0][1][4];                     //      000
        array[10] = cube[2][1][4]*10 + cube[1][0][3];                    //      000
        array[11] = cube[2][1][0]*10 + cube[1][2][3];
        return array;
    }

    private int [] initializationCornerParticles(){
        int [] cornerPartsOfCube = new int[8];
        cornerPartsOfCube[0] = cube[2][0][1]*100 + cube[0][0][2]*10 + cube[0][2][4];
        cornerPartsOfCube[1] = cube[0][0][3]*100 + cube[2][0][2]*10 + cube[2][2][4];
        cornerPartsOfCube[2] = cube[2][2][1]*100 + cube[0][2][2]*10 + cube[0][0][0];
        cornerPartsOfCube[3] = cube[0][2][3]*100 + cube[2][2][2]*10 + cube[2][0][0];
        cornerPartsOfCube[4] = cube[2][0][3]*100 + cube[2][0][5]*10 + cube[2][0][4];
        cornerPartsOfCube[5] = cube[0][0][5]*100 + cube[0][0][4]*10 + cube[0][0][1];
        cornerPartsOfCube[6] = cube[2][2][5]*100 + cube[2][2][0]*10 + cube[2][2][3];
        cornerPartsOfCube[7] = cube[0][2][5]*100 + cube[0][2][0]*10 + cube[0][2][1];
        return cornerPartsOfCube;
    }

    private void position44Solve(){    //place 44 from 12 variants (on pic1)
        int[] twoParticles = initializationTwoParticles();
        int rightValue = cube[1][1][0]*10 + cube[1][1][2];
        int rightReversedValue = cube[1][1][0] + cube[1][1][2]*10;
        if (twoParticles[3] != rightValue){
            if (twoParticles[0] == rightValue){
                verticalMoveReverse(0);
                moveSideUP();
                horizontalMoveRight(2);
                moveSideDown();
                horizontalMoveReverse(2);
            } else
            if (twoParticles[0] == rightReversedValue){
                verticalMoveDown(0);
                horizontalMoveRight(2);
            } else
            if (twoParticles[1] == rightValue){
                horizontalMoveReverse(0);
                moveSideUP();
                horizontalMoveReverse(2);
                moveSideDown();
                horizontalMoveReverse(2);
            } else
            if (twoParticles[1] == rightReversedValue){
                verticalMoveDown(1);
                horizontalMoveRight(2);
                verticalMoveUP(1);
                horizontalMoveLeft(2);
            } else
            if (twoParticles[2] == rightValue){
                verticalMoveDown(2);
                horizontalMoveLeft(2);
            } else
            if (twoParticles[2] == rightReversedValue){
                verticalMoveReverse(2);
                moveSideUP();
                horizontalMoveLeft(2);
                moveSideDown();
                horizontalMoveReverse(2);
            } else
            if (twoParticles[3] == rightReversedValue){
                horizontalMoveLeft(2);
                rotateBottomPositiveAxis();
                verticalMoveUP(0);
                rotateBottomNegativeAxis();
            } else
            if (twoParticles[4] == rightValue){
                verticalMoveUP(0);
                horizontalMoveRight(2);
                verticalMoveDown(0);
            } else
            if (twoParticles[4] == rightReversedValue){
                moveSideUP();
                horizontalMoveRight(2);
                moveSideDown();
                horizontalMoveReverse(2);
            } else
            if (twoParticles[5] == rightValue){
                rotateDownSideNegativeAxis();
                verticalMoveUP(0);
                horizontalMoveRight(2);
                verticalMoveDown(0);
            } else
            if (twoParticles[5] == rightReversedValue){
                rotateDownSideNegativeAxis();
                moveSideUP();
                horizontalMoveRight(2);
                moveSideDown();
                horizontalMoveReverse(2);
            } else
            if (twoParticles[6] == rightValue){
                rotateDownSidePositiveAxis();
                horizontalMoveReverse(2);
            } else
            if (twoParticles[6] == rightReversedValue){
                verticalMoveUP(2);
                horizontalMoveLeft(2);
                verticalMoveDown(2);
            } else
            if (twoParticles[7] == rightValue){
                rotateDownSidePositiveAxis();
                verticalMoveUP(0);
                horizontalMoveRight(2);
                verticalMoveDown(0);
            } else
            if (twoParticles[7] == rightReversedValue){
                horizontalMoveReverse(2);
            } else
            if (twoParticles[8] == rightValue){
                rotateBottomPositiveAxis();
                verticalMoveUP(0);
                rotateBottomNegativeAxis();
            } else
            if (twoParticles[8] == rightReversedValue){
                horizontalMoveRight(2);
            } else
            if (twoParticles[9] == rightValue){
                rotateBottomPositiveAxis();
                verticalMoveDown(0);
                rotateBottomNegativeAxis();
            } else
            if (twoParticles[9] == rightReversedValue){
                rotateBottomPositiveAxis();
                rotateBottomPositiveAxis();
                horizontalMoveRight(0);
                rotateBottomNegativeAxis();
                rotateBottomNegativeAxis();
            } else
            if (twoParticles[10] == rightValue){
                rotateBottomPositiveAxis();
                rotateBottomPositiveAxis();
                horizontalMoveLeft(0);
                rotateBottomNegativeAxis();
                rotateBottomNegativeAxis();
            } else
            if (twoParticles[10] == rightReversedValue){
                rotateBottomNegativeAxis();
                verticalMoveDown(2);
                rotateBottomPositiveAxis();
            } else
            if (twoParticles[11] == rightValue){
                horizontalMoveLeft(2);
            } else
            if (twoParticles[11] == rightReversedValue){
                rotateBottomNegativeAxis();
                verticalMoveUP(2);
                rotateBottomPositiveAxis();
            } else {
                System.err.println("test_ERROR!! 44");
                cubeOut();
            }
        }
    }

    private void firstStepSolve(){
        for (int i = 0; i < 3; i++){
            position44Solve();
            rotateAllPositiveAxis();
        }
        position44Solve();
    }

    private boolean compareThreeNumberValues(int value1, int value2){   //321 == 321 || 132 || 213  ||..
        return (value1 == value2 ||
                value1 == (value2%10)*100 + (value2%100/10)*10 + (value2/100) ||
                value1 == (value2%10)*10 + (value2%100/10) + (value2/100)*100 ||
                value1 == (value2%10)*100 + (value2%100/10) + (value2/100)*10 ||
                value1 == (value2%10)*10 + (value2%100/10)*100 + (value2/100) ||
                value1 == (value2%10) + (value2%100/10)*100 + (value2/100)*10 ||
                value1 == (value2%10) + (value2%100/10)*10 + (value2/100)*100
        );

    }

    private void position333Solve(){
        if (cube[2][0][0] == cube[1][1][2]){
            verticalMoveDown(2);
            rotateDownSideNegativeAxis();
            verticalMoveReverse(2);
            horizontalMoveLeft(2);
            verticalMoveDown(2);
            horizontalMoveRight(2);
        } else
        if (cube[0][2][3] == cube[1][1][2]){
            horizontalMoveRight(2);
            rotateDownSidePositiveAxis();
            horizontalMoveReverse(2);
            verticalMoveUP(2);
            horizontalMoveRight(2);
            verticalMoveDown(2);
        } else
        if (cube[2][2][2] != cube[1][1][2]){
                System.err.println("test_ERROR!!!! in position333Solve");
            }
    }

    private void replace333_666(){
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
    }

    private void position333TotalSolve(){
        int[] threeParticles = initializationCornerParticles();
        int rightValue = cube[1][1][0] + cube[1][1][2]*10 + cube[1][1][3]*100;
        if (threeParticles[3] != rightValue){
            if (compareThreeNumberValues(rightValue,threeParticles[0])){
                verticalMoveUP(0);
                rotateDownSidePositiveAxis();
                rotateDownSidePositiveAxis();
                verticalMoveDown(0);
                replace333_666();
            } else
            if (compareThreeNumberValues(rightValue,threeParticles[1])){
                verticalMoveUP(2);
                rotateDownSideNegativeAxis();
                verticalMoveDown(2);
                rotateDownSideNegativeAxis();
                rotateDownSideNegativeAxis();
                replace333_666();
            } else
            if (compareThreeNumberValues(rightValue,threeParticles[2])){
                verticalMoveDown(0);
                rotateDownSideNegativeAxis();
                verticalMoveUP(0);
                replace333_666();
            } else
            if (compareThreeNumberValues(rightValue,threeParticles[4])){
                rotateDownSidePositiveAxis();
                replace333_666();
            } else
            if (compareThreeNumberValues(rightValue,threeParticles[5])){
                rotateDownSidePositiveAxis();
                rotateDownSidePositiveAxis();
                replace333_666();
            } else
            if (compareThreeNumberValues(rightValue,threeParticles[6])){
                replace333_666();
            } else
            if (compareThreeNumberValues(rightValue,threeParticles[7])){
                rotateDownSideNegativeAxis();
                replace333_666();
            } else
            if (!compareThreeNumberValues(rightValue,threeParticles[3])){
                System.out.println("test_DOESN'T_WORK in position333TotalSolve");
                System.out.println(rightValue + "  rightValue");
                cubeOut();
            }


            position333Solve();
        }
    }

    private void secondStepSolve(){
        for (int i = 0; i < 3; i++){
            position333TotalSolve();
            rotateAllPositiveAxis();
        }
        position333TotalSolve();
    }

    private void middleLeftMove(){  //pic2
        horizontalMoveRight(2);
        verticalMoveDown(0);
        horizontalMoveLeft(2);
        verticalMoveUP(0);
        horizontalMoveLeft(2);
        rotateBottomNegativeAxis();
        horizontalMoveRight(2);
        rotateBottomPositiveAxis();
    }

    private void middleRightMove(){  //pic2
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveRight(2);
        rotateBottomPositiveAxis();
        horizontalMoveLeft(2);
        rotateBottomNegativeAxis();
    }

    private void thirdStepSolve(){         // middle level
        moveSideUP();
        int j = 0;
        while (!checkTurnedUPWinMiddleSide()){
            if (cube[0][1][1] != cube[1][1][0] && cube[2][1][1] != cube[1][1][0] &&
                    cube[0][1][2] != cube[1][1][0] && cube[2][1][2] != cube[1][1][0] &&
                    cube[0][1][3] != cube[1][1][0] && cube[2][1][3] != cube[1][1][0] &&
                    cube[0][1][5] != cube[1][1][0] && cube[2][1][5] != cube[1][1][0] ){
                if (cube[0][1][1] != cube[1][1][0]){
                    moveSideToTheRight();
                    middleLeftMove();
                }
                if (cube[2][1][1] != cube[1][1][0]){
                    moveSideToTheRight();
                    middleRightMove();
                }
                if (cube[0][1][2] != cube[1][1][0]){
                    middleLeftMove();
                }
                if (cube[2][1][2] != cube[1][1][0]){
                    middleRightMove();
                }
                if (cube[0][1][3] != cube[1][1][0]){
                    moveSideToTheLeft();
                    middleLeftMove();
                }
                if (cube[2][1][3] != cube[1][1][0]){
                    moveSideToTheLeft();
                    middleRightMove();
                }
                if (cube[0][1][5] != cube[1][1][0]){
                    moveSideToTheLeft();
                    moveSideToTheLeft();
                    middleRightMove();
                }
                if (cube[2][1][5] != cube[1][1][0]){
                    moveSideToTheLeft();
                    moveSideToTheLeft();
                    middleLeftMove();
                }
            }
            j++;
            for (int i = 0; i < 6; i++){
                if (cube[1][2][2] == cube[1][1][2] && cube[1][0][0] == cube[1][1][1]){middleLeftMove();}
                if (cube[1][2][2] == cube[1][1][2] && cube[1][0][0] == cube[1][1][3]){middleRightMove();}
                horizontalMoveRight(2);
            }
            moveSideToTheRight();
            if (j>100){
                System.err.println("ERR j > 100 in thirdStep");
                break;
            }
        }
        moveSideDown();
    }

    private void rotateFaceSideToLeft(boolean iaPrinted){
        rotateAllPositiveAxis(false);
        verticalMoveUP(0, false);
        rotateAllNegativeSide(false);
        if (iaPrinted){
            printStepSolve("rotateFaceSideToLeft");
        }
    }

    private void rotateFaceSideToLeft(){
        rotateFaceSideToLeft(true);
    }

    private void rotateFaceSideToRight(boolean isPrinted){
        rotateAllPositiveAxis(false);
        verticalMoveDown(0, false);
        rotateAllNegativeSide(false);
        if (isPrinted){
            printStepSolve("rotateFaceSideToRight");
        }
    }

    private void rotateFaceSideToRight(){
        rotateFaceSideToRight(true);
    }                                           //

    private void topMiddleMove(){
        rotateBottomPositiveAxis();
        rotateFaceSideToRight();
        verticalMoveUP(2);
        rotateBottomPositiveAxis();
        verticalMoveDown(2);
        rotateBottomNegativeAxis();
        rotateFaceSideToLeft();
    }

    private void topMiddleRotateCorner(){
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        rotateBottomNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        rotateBottomPositiveAxis();
    }

    private void topMiddleRotateLinear(){
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        rotateBottomNegativeAxis();
        rotateBottomNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        verticalMoveUP(2);
        rotateMiddleNegativeAxis();
        rotateBottomPositiveAxis();
        rotateBottomPositiveAxis();
    }

    private void topMiddleSolve(){
        int j = 0;
        moveSideUP();//todo del
        moveSideUP();

        if (cube[2][1][2] == cube[1][1][0] || cube[2][1][3] == cube[1][1][0]){
            rotateBottomPositiveAxis();
        }
        if (cube[0][1][2] == cube[1][1][0] || cube[0][1][1] == cube[1][1][0]){
            rotateBottomNegativeAxis();
        }
        if (cube[1][0][2] == cube[1][1][0] || cube[1][2][4] == cube[1][1][0]){
            rotateBottomPositiveAxis();
            rotateBottomPositiveAxis();
        }

        while (!(cube[1][2][2] == cube[1][1][0] || cube[1][0][0] == cube[1][1][0]) ||
                !(cube[0][1][2] == cube[1][1][1] || cube[2][1][1] == cube[1][1][1]) ||
                !(cube[1][0][2] == cube[1][1][4] || cube[1][2][4] == cube[1][1][4]) ||
                !(cube[2][1][2] == cube[1][1][3] || cube[0][1][3] == cube[1][1][3])) {
            j++;
            if(j>100){
                System.err.println("test j>100 top middle-------------------------------------------------");
                cubeOut();
                break;
            }
            if ((cube[1][2][2] == cube[1][1][3] || cube[1][0][0] == cube[1][1][3]) &&
                    (cube[0][1][2] == cube[1][1][0] || cube[2][1][1] == cube[1][1][0]) &&
                    (cube[1][0][2] == cube[1][1][1] || cube[1][2][4] == cube[1][1][1]) &&
                    (cube[2][1][2] == cube[1][1][4] || cube[0][1][3] == cube[1][1][4])) {
                topMiddleMove();
                rotateBottomNegativeAxis();
                topMiddleMove();
                rotateBottomNegativeAxis();
                topMiddleMove();
                rotateBottomNegativeAxis();
                rotateBottomNegativeAxis();
            }
            if (cube[1][2][2] == cube[1][1][1] || cube[1][0][0] == cube[1][1][1]){
                topMiddleMove();
            }
            if (cube[1][2][2] == cube[1][1][4] || cube[1][0][0] == cube[1][1][4]){
                topMiddleMove();
                rotateBottomNegativeAxis();
                topMiddleMove();
                rotateBottomPositiveAxis();
                topMiddleMove();
            }
            rotateAllPositiveAxis();
        }

        j = 0;
        while (cube[0][1][2] != cube[1][0][2] || cube[1][0][2] != cube[1][2][2] || cube[1][2][2] != cube[2][1][2]){
            j++;
            if(j>100){
                System.err.println("test j>100 top middleN2-------------------------------------------------");
                cubeOut();
                break;
            }
            if (cube[1][2][2] != cube[1][1][2] && cube[2][1][2] != cube[1][1][2]){
                topMiddleRotateCorner();
            }
            if (cube[0][1][2] != cube[1][1][2] && cube[2][1][2] != cube[1][1][2]){
                topMiddleRotateLinear();
            }
            rotateAllPositiveAxis();
        }

        moveSideUP();//todo del
        moveSideUP();
    }

    private void topCornerMoveLeft(){
        horizontalMoveLeft(2);
        verticalMoveUP(0);
        horizontalMoveRight(2);
        verticalMoveDown(2);
        horizontalMoveLeft(2);
        verticalMoveDown(0);
        horizontalMoveRight(2);
        verticalMoveUP(2);
    }

    private void topCornerMoveRight(){
        verticalMoveDown(2);
        horizontalMoveLeft(2);
        verticalMoveUP(0);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(0);
        horizontalMoveRight(2);
    }

    private void topCornerRotateOneByOne(){
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        rotateBottomNegativeAxis();
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        rotateBottomPositiveAxis();
    }

    private void topCornerRotateOpposite(){
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        rotateBottomNegativeAxis();
        rotateBottomNegativeAxis();
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        verticalMoveUP(2);
        horizontalMoveLeft(2);
        verticalMoveDown(2);
        horizontalMoveRight(2);
        rotateBottomPositiveAxis();
        rotateBottomPositiveAxis();
    }

    private boolean topCornerIsPlaced(){
        return (cube[2][2][2] == cube[1][1][0] || cube[2][0][0] == cube[1][1][0] || cube[0][2][3] == cube[1][1][0]) &&
                (cube[2][2][2] == cube[1][1][3] || cube[2][0][0] == cube[1][1][3] || cube[0][2][3] == cube[1][1][3]) &&
                (cube[0][2][2] == cube[1][1][0] || cube[0][0][0] == cube[1][1][0] || cube[2][2][1] == cube[1][1][0]) &&
                (cube[0][2][2] == cube[1][1][1] || cube[0][0][0] == cube[1][1][1] || cube[2][2][1] == cube[1][1][1]) &&
                (cube[0][0][2] == cube[1][1][1] || cube[2][0][1] == cube[1][1][1] || cube[0][2][4] == cube[1][1][1]) &&
                (cube[0][0][2] == cube[1][1][4] || cube[2][0][1] == cube[1][1][4] || cube[0][2][4] == cube[1][1][4]);
    }

    int topInitializationCornerParts(){
        int result = 0;
        if (cube[0][0][0] == cube[1][1][0] && cube[0][2][2] == cube[1][1][1] ||
                cube[0][0][0] == cube[1][1][1] && cube[0][2][2] == cube[1][1][0] ||
                cube[0][0][0] == cube[1][1][0] && cube[2][2][1] == cube[1][1][1] ||
                cube[0][0][0] == cube[1][1][1] && cube[2][2][1] == cube[1][1][0] ||
                cube[2][2][1] == cube[1][1][1] && cube[0][2][2] == cube[1][1][0] ||
                cube[2][2][1] == cube[1][1][0] && cube[0][2][2] == cube[1][1][1]
                ){result += 1000;}
        if (cube[0][0][0] == cube[1][1][4] && cube[0][2][2] == cube[1][1][1] ||
                cube[0][0][0] == cube[1][1][1] && cube[0][2][2] == cube[1][1][4] ||
                cube[0][0][0] == cube[1][1][4] && cube[2][2][1] == cube[1][1][1] ||
                cube[0][0][0] == cube[1][1][1] && cube[2][2][1] == cube[1][1][4] ||
                cube[2][2][1] == cube[1][1][1] && cube[0][2][2] == cube[1][1][4] ||
                cube[2][2][1] == cube[1][1][4] && cube[0][2][2] == cube[1][1][1]
                ){result += 2000;}
        if (cube[0][0][0] == cube[1][1][4] && cube[0][2][2] == cube[1][1][3] ||
                cube[0][0][0] == cube[1][1][3] && cube[0][2][2] == cube[1][1][4] ||
                cube[0][0][0] == cube[1][1][4] && cube[2][2][1] == cube[1][1][3] ||
                cube[0][0][0] == cube[1][1][3] && cube[2][2][1] == cube[1][1][4] ||
                cube[2][2][1] == cube[1][1][3] && cube[0][2][2] == cube[1][1][4] ||
                cube[2][2][1] == cube[1][1][4] && cube[0][2][2] == cube[1][1][3]
                ){result += 3000;}
        if (cube[0][0][0] == cube[1][1][0] && cube[0][2][2] == cube[1][1][3] ||
                cube[0][0][0] == cube[1][1][3] && cube[0][2][2] == cube[1][1][0] ||
                cube[0][0][0] == cube[1][1][0] && cube[2][2][1] == cube[1][1][3] ||
                cube[0][0][0] == cube[1][1][3] && cube[2][2][1] == cube[1][1][0] ||
                cube[2][2][1] == cube[1][1][3] && cube[0][2][2] == cube[1][1][0] ||
                cube[2][2][1] == cube[1][1][0] && cube[0][2][2] == cube[1][1][3]
                ){result += 4000;}

        if (cube[0][0][2] == cube[1][1][0] && cube[0][2][4] == cube[1][1][1] ||
                cube[0][0][2] == cube[1][1][1] && cube[0][2][4] == cube[1][1][0] ||
                cube[0][0][2] == cube[1][1][0] && cube[2][0][1] == cube[1][1][1] ||
                cube[0][0][2] == cube[1][1][1] && cube[2][0][1] == cube[1][1][0] ||
                cube[2][0][1] == cube[1][1][1] && cube[0][2][4] == cube[1][1][0] ||
                cube[2][0][1] == cube[1][1][0] && cube[0][2][4] == cube[1][1][1]
                ){result += 100;}
        if (cube[0][0][2] == cube[1][1][4] && cube[0][2][4] == cube[1][1][1] ||
                cube[0][0][2] == cube[1][1][1] && cube[0][2][4] == cube[1][1][4] ||
                cube[0][0][2] == cube[1][1][4] && cube[2][0][1] == cube[1][1][1] ||
                cube[0][0][2] == cube[1][1][1] && cube[2][0][1] == cube[1][1][4] ||
                cube[2][0][1] == cube[1][1][1] && cube[0][2][4] == cube[1][1][4] ||
                cube[2][0][1] == cube[1][1][4] && cube[0][2][4] == cube[1][1][1]
                ){result += 200;}
        if (cube[0][0][2] == cube[1][1][4] && cube[0][2][4] == cube[1][1][3] ||
                cube[0][0][2] == cube[1][1][3] && cube[0][2][4] == cube[1][1][4] ||
                cube[0][0][2] == cube[1][1][4] && cube[2][0][1] == cube[1][1][3] ||
                cube[0][0][2] == cube[1][1][3] && cube[2][0][1] == cube[1][1][4] ||
                cube[2][0][1] == cube[1][1][3] && cube[0][2][4] == cube[1][1][4] ||
                cube[2][0][1] == cube[1][1][4] && cube[0][2][4] == cube[1][1][3]
                ){result += 300;}
        if (cube[0][0][2] == cube[1][1][0] && cube[0][2][4] == cube[1][1][3] ||
                cube[0][0][2] == cube[1][1][3] && cube[0][2][4] == cube[1][1][0] ||
                cube[0][0][2] == cube[1][1][0] && cube[2][0][1] == cube[1][1][3] ||
                cube[0][0][2] == cube[1][1][3] && cube[2][0][1] == cube[1][1][0] ||
                cube[2][0][1] == cube[1][1][3] && cube[0][2][4] == cube[1][1][0] ||
                cube[2][0][1] == cube[1][1][0] && cube[0][2][4] == cube[1][1][3]
                ){result += 400;}

        if (cube[0][0][3] == cube[1][1][0] && cube[2][2][4] == cube[1][1][1] ||
                cube[0][0][3] == cube[1][1][1] && cube[2][2][4] == cube[1][1][0] ||
                cube[0][0][3] == cube[1][1][0] && cube[2][0][2] == cube[1][1][1] ||
                cube[0][0][3] == cube[1][1][1] && cube[2][0][2] == cube[1][1][0] ||
                cube[2][0][2] == cube[1][1][1] && cube[2][2][4] == cube[1][1][0] ||
                cube[2][0][2] == cube[1][1][0] && cube[2][2][4] == cube[1][1][1]
                ){result += 10;}
        if (cube[0][0][3] == cube[1][1][4] && cube[2][2][4] == cube[1][1][1] ||
                cube[0][0][3] == cube[1][1][1] && cube[2][2][4] == cube[1][1][4] ||
                cube[0][0][3] == cube[1][1][4] && cube[2][0][2] == cube[1][1][1] ||
                cube[0][0][3] == cube[1][1][1] && cube[2][0][2] == cube[1][1][4] ||
                cube[2][0][2] == cube[1][1][1] && cube[2][2][4] == cube[1][1][4] ||
                cube[2][0][2] == cube[1][1][4] && cube[2][2][4] == cube[1][1][1]
                ){result += 20;}
        if (cube[0][0][3] == cube[1][1][4] && cube[2][2][4] == cube[1][1][3] ||
                cube[0][0][3] == cube[1][1][3] && cube[2][2][4] == cube[1][1][4] ||
                cube[0][0][3] == cube[1][1][4] && cube[2][0][2] == cube[1][1][3] ||
                cube[0][0][3] == cube[1][1][3] && cube[2][0][2] == cube[1][1][4] ||
                cube[2][0][2] == cube[1][1][3] && cube[2][2][4] == cube[1][1][4] ||
                cube[2][0][2] == cube[1][1][4] && cube[2][2][4] == cube[1][1][3]
                ){result += 30;}
        if (cube[0][0][3] == cube[1][1][0] && cube[2][2][4] == cube[1][1][3] ||
                cube[0][0][3] == cube[1][1][3] && cube[2][2][4] == cube[1][1][0] ||
                cube[0][0][3] == cube[1][1][0] && cube[2][0][2] == cube[1][1][3] ||
                cube[0][0][3] == cube[1][1][3] && cube[2][0][2] == cube[1][1][0] ||
                cube[2][0][2] == cube[1][1][3] && cube[2][2][4] == cube[1][1][0] ||
                cube[2][0][2] == cube[1][1][0] && cube[2][2][4] == cube[1][1][3]
                ){result += 40;}

        if (cube[0][2][3] == cube[1][1][0] && cube[2][0][0] == cube[1][1][1] ||
                cube[0][2][3] == cube[1][1][1] && cube[2][0][0] == cube[1][1][0] ||
                cube[0][2][3] == cube[1][1][0] && cube[2][2][2] == cube[1][1][1] ||
                cube[0][2][3] == cube[1][1][1] && cube[2][2][2] == cube[1][1][0] ||
                cube[2][2][2] == cube[1][1][1] && cube[2][0][0] == cube[1][1][0] ||
                cube[2][2][2] == cube[1][1][0] && cube[2][0][0] == cube[1][1][1]
                ){result += 1;}
        if (cube[0][2][3] == cube[1][1][4] && cube[2][0][0] == cube[1][1][1] ||
                cube[0][2][3] == cube[1][1][1] && cube[2][0][0] == cube[1][1][4] ||
                cube[0][2][3] == cube[1][1][4] && cube[2][2][2] == cube[1][1][1] ||
                cube[0][2][3] == cube[1][1][1] && cube[2][2][2] == cube[1][1][4] ||
                cube[2][2][2] == cube[1][1][1] && cube[2][0][0] == cube[1][1][4] ||
                cube[2][2][2] == cube[1][1][4] && cube[2][0][0] == cube[1][1][1]
                ){result += 2;}
        if (cube[0][2][3] == cube[1][1][4] && cube[2][0][0] == cube[1][1][3] ||
                cube[0][2][3] == cube[1][1][3] && cube[2][0][0] == cube[1][1][4] ||
                cube[0][2][3] == cube[1][1][4] && cube[2][2][2] == cube[1][1][3] ||
                cube[0][2][3] == cube[1][1][3] && cube[2][2][2] == cube[1][1][4] ||
                cube[2][2][2] == cube[1][1][3] && cube[2][0][0] == cube[1][1][4] ||
                cube[2][2][2] == cube[1][1][4] && cube[2][0][0] == cube[1][1][3]
                ){result += 3;}
        if (cube[0][2][3] == cube[1][1][0] && cube[2][0][0] == cube[1][1][3] ||
                cube[0][2][3] == cube[1][1][3] && cube[2][0][0] == cube[1][1][0] ||
                cube[0][2][3] == cube[1][1][0] && cube[2][2][2] == cube[1][1][3] ||
                cube[0][2][3] == cube[1][1][3] && cube[2][2][2] == cube[1][1][0] ||
                cube[2][2][2] == cube[1][1][3] && cube[2][0][0] == cube[1][1][0] ||
                cube[2][2][2] == cube[1][1][0] && cube[2][0][0] == cube[1][1][3]
                ){result += 4;}
        return result;
    }

    private void topCornerSolve(){
        moveSideUP();//todo del
        moveSideUP();
        int position = topInitializationCornerParts();
            if (position %10 == 3 && position /1000 == 4 && position /10%10 == 1){
                topCornerMoveLeft();
                position = topInitializationCornerParts();
            }
            if (position %10 == 1 && position /1000 == 3 && position /10%10 == 4){
                topCornerMoveRight();
                position = topInitializationCornerParts();
            }
            if (position /10%10 == 1){
                topCornerMoveLeft();
                position = topInitializationCornerParts();
            }
            if (position %10 == 1){
                topCornerMoveRight();
                position = topInitializationCornerParts();
            }
            if (position /100%10 == 1){
                rotateBottomNegativeAxis();
                topCornerMoveLeft();
                rotateBottomPositiveAxis();
                position = topInitializationCornerParts();
            }
            if (position %10 == 2){
                rotateBottomPositiveAxis();
                topCornerMoveRight();
                rotateBottomNegativeAxis();
                position = topInitializationCornerParts();
            }
            if (position /10%10 == 2){
                rotateBottomPositiveAxis();
                topCornerMoveLeft();
                rotateBottomNegativeAxis();
                position = topInitializationCornerParts();
            }
            if (position /10%10 == 4){
                rotateBottomPositiveAxis();
                topCornerMoveRight();
                rotateBottomNegativeAxis();
                rotateBottomNegativeAxis();
                topCornerMoveRight();
                rotateBottomPositiveAxis();
            }
        if (!topCornerIsPlaced()){
            System.err.println("testERR topCornerIsPlaced------------------------------");
            System.err.println(position);
        }

        int j = 0;
        while (cube[0][0][2] != cube[1][1][2] || cube[0][2][2] != cube[1][1][2] || cube[2][2][2] != cube[1][1][2]){
            j++;
            if (j > 100){
                System.err.println("testErr topCornerRotate-------------------------------");
                cubeOut();
                break;
            }
            if (cube[2][2][2] == cube[1][1][3] && cube[0][2][2] == cube[1][1][1]){
                topCornerRotateOneByOne();
            }
            if (cube[2][2][2] == cube[1][1][3] && cube[0][0][2] == cube[1][1][4]){
                topCornerRotateOpposite();
            }
            if (cube[2][2][2] == cube[1][1][3] && cube[0][2][2] != cube[1][1][2]){
                topCornerRotateOneByOne();
            }
            if (cube[2][2][2] == cube[1][1][3] && cube[0][0][2] != cube[1][1][2]){
                topCornerRotateOpposite();
            }
            if (cube[2][2][2] == cube[1][1][0] && cube[0][0][2] != cube[1][1][2]){
                topCornerRotateOpposite();
                topCornerRotateOpposite();
            }
            if (cube[2][2][2] == cube[1][1][0] && cube[0][2][2] != cube[1][1][2]){
                topCornerRotateOneByOne();
                topCornerRotateOneByOne();
            }
            rotateAllPositiveAxis();
        }

        moveSideUP();//todo del
        moveSideUP();
    }

    public void solveCube(){
        firstStepSolve();
        secondStepSolve();
        thirdStepSolve();
        topMiddleSolve();
        topCornerSolve();
        if (!checkWin()){
            System.err.println("BAD NEWS-------------------------------error");
        }else {
            System.out.println("Number of steps is " + numberOfSteps);
        }

    }

    private void printStepSolve(String name){
        System.out.println(name);
        numberOfSteps++;
    }

    public static void main(String[] args)  throws Exception{
        Cube cube1 = new Cube(false);
        cube1.solveCube();
        cube1.cubeOut();
    }
}
