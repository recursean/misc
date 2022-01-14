public class Rotate90 {
    int[][] rotate90(int[][] matrix) {
        int[][] rotated = new int[matrix.length][matrix[0].length];

        int newRow = 0;
        int newCol = matrix.length - 1;

        for(int row = 0; row < matrix.length; row++) {
            for(int col = 0; col < matrix[0].length; col++) {
                rotated[newRow++][newCol] = matrix[row][col];
            }

            newRow = 0;
            newCol--;
        }

        return rotated;
    }
    public static void main(String[] args) {
        int[][] matrix = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 9 }
        };

        matrix = new Rotate90().rotate90(matrix);

        for(int row = 0; row < matrix.length; row++) {
            for(int col = 0; col < matrix[0].length; col++) {
                System.out.print(matrix[row][col] + " ");
            }
            System.out.println();
        }
    }
}