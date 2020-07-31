import java.util.ArrayList;
import java.util.Scanner;

public class LinearAlgebraBonus1
{
	public static void main(String[] args)
	{
		Scanner scan = new Scanner(System.in);
		float[][] augmentedMatrix = new float[3][4];

		System.out.println("RREF Calculator for 3x4 Augmented Matrices by Jason James");
		for (int i = 0; i < augmentedMatrix.length; i++)
		{
			System.out.println("Enter Row " + (i+1));
			for (int j = 0; j < augmentedMatrix[i].length; j++)
			{
				if (j == 3)
				{
					System.out.println("Enter B" + (i+1));
				}
				else
				{
					System.out.println("Enter A" + (i+1) + (j+1));
				}

				augmentedMatrix[i][j] = scan.nextFloat();
			}
		}

		printMatrix(augmentedMatrix);
		int swapCount = 0;
		boolean keepLooping = true;

		while (!isRREF(augmentedMatrix) && keepLooping)
		{
			// Default conditions for whether a row operation has taken place in iteration or not
			boolean swapped = false;
			boolean added = false;
			boolean multiplied = false;
			boolean containsZeroRow = false;

			for (int i = 0; i < augmentedMatrix.length; i++)
			{
				if (isZeroRow(augmentedMatrix, i))
				{
					System.out.println("\n\n\t\tINFINITE SOLUTIONS");
					containsZeroRow = true;
					keepLooping = false;
					break;
				}
			}

			if (containsZeroRow)
			{
				keepLooping = false;
				break;
			}

			// Check to see if row swap operation can be executed
			if (swapCount < 2)
			{
				for (int i = 0; i < augmentedMatrix.length; i++)
				{
					for (int j = 0; j < augmentedMatrix[i].length - 1; j++)
					{
						if (i == j && augmentedMatrix[i][j] == 0 && !swapped)
						{
							for (int k = 0; k < augmentedMatrix.length; k++)
							{
								if (i != k && augmentedMatrix[k][j] != 0 && augmentedMatrix[k][k] == 0)
								{
									swapRows(augmentedMatrix, i, k);
									swapped = true;
									break;
								}
							}
						}
					}
				}
			}

			// Check to see if row addition operation can be executed
			// If previous row operations have not been executed
			if (!swapped && !multiplied)
			{
				int[] leadIndexArr = new int[3];
				// For loop assigns each row a lead index  value based on the position of the leading number
				for (int i = 0; i < augmentedMatrix.length; i++)
				{
					int leadIndex = -1; // Impossible index value used as default value
					for (int j = 0; j < augmentedMatrix[i].length - 1; j++)
					{
						if (augmentedMatrix[i][j] != 0 && leadIndex == -1)
						{
							leadIndex = j;
						}
						leadIndexArr[i] = leadIndex;
					}
				}

				for (int column = 0; column < 3; column++)
				{
					for (int row = 0; row < augmentedMatrix.length; row++)
					{
						if (column == row && augmentedMatrix[row][column] != 0 && augmentedMatrix[row][column] != 1 && !multiplied & !added)
						{
							float var = augmentedMatrix[row][column];
							float multiple = 1.0f / var;
							multiplyRow(augmentedMatrix, row, multiple);
							multiplied = true;
							break;
						}
					}
					for (int row = 0; row < augmentedMatrix.length; row++)
					{
						ArrayList<Integer> pivotRows = new ArrayList<>();
						for (int i = 0; i < augmentedMatrix.length; i++)
						{
							if (isPivotRow(augmentedMatrix, i))
							{
								pivotRows.add(i);
							}
						}

						if (column == row && augmentedMatrix[row][column] != 1 && !multiplied && !added)
						{
							for (int k = 0; k < augmentedMatrix.length; k++)
							{
								if (row != k && augmentedMatrix[k][column] != 0 && augmentedMatrix[k][k] == 0)
								{
									float multiple = (1.0f - augmentedMatrix[row][column]) / augmentedMatrix[k][column];
									addRow(augmentedMatrix, row, k, multiple);
									added = true;
									break;
								}
							}
						}

						else if (column != row && augmentedMatrix[row][column] != 0 && !multiplied && !added)
						{
							for (int k = 0; k < augmentedMatrix.length; k++)
							{
								if (pivotRows.contains(k))
								{
									if (row != k && augmentedMatrix[k][column] != 0 && !added)
									{
										float multiple = (0.0f - augmentedMatrix[row][column]) / augmentedMatrix[k][column];
										addRow(augmentedMatrix, row, k, multiple);
										added = true;
										break;
									}
								}
							}

							for (int k = 0; k < augmentedMatrix.length; k++)
							{
								if (row != k && augmentedMatrix[k][column] != 0 && !added)
								{
									float multiple = (0.0f - augmentedMatrix[row][column]) / augmentedMatrix[k][column];
									addRow(augmentedMatrix, row, k, multiple);
									added = true;
									break;
								}
							}
						}
					}
				}
			}

			// If row operation was performed, print matrix
			if (swapped || added || multiplied)
			{
				System.out.println("\n\n\t\t|\n\t\t|\n\t\tV");
				printMatrix(augmentedMatrix);
			}

			else
			{
				for (int i = 0; i < augmentedMatrix.length; i++)
				{
					if (isZeroRow(augmentedMatrix, i))
					{
						containsZeroRow = true;
						break;
					}
				}

				if (containsZeroRow)
				{
					System.out.println("\n\n\t\tINFINITE SOLUTIONS");
					keepLooping = false;
				}

				if (!containsZeroRow)
				{
					System.out.println("\n\n\t\tNO SOLUTION");
					keepLooping = false;
				}

				break;
			}
		}
	}

	private static boolean isPivotRow(float[][] matrix, int row)
	{
		for (int i = 0; i < matrix[row].length - 1; i++)
		{
			if (!isElementRREFCompliant(matrix, row, i))
			{
				return false;
			}
		}

		return true;
	}

	// Elementary row operation of multiplying a row by any non-zero constant
	private static float[][] multiplyRow(float[][] matrix, int currentRowIndex, float multiple)
	{
		float[] currentRow = matrix[currentRowIndex];

		if (multiple == 0.0)
		{
			System.out.println("ERROR: Cannot multiply row by zero");
			return matrix;
		}

		for (int i = 0; i < currentRow.length; i ++)
		{
			float product = currentRow[i] * multiple;
			currentRow[i] = product;
		}

		return matrix;
	}


	// Elementary row operation of adding any non-zero multiple of row at multipleRowIndex
	// to row at currentRowIndex to form new row at currentRowIndex
	private static float[][] addRow(float[][] matrix, int currentRowIndex, int multipleRowIndex, float multiple)
	{
		float[] currentRow = matrix[currentRowIndex];
		float[] multipleRow = matrix[multipleRowIndex];

		if (multiple == 0.0)
		{
			System.out.println("ERROR: Cannot add row by row of multiple of zero");
			return matrix;
		}

		for(int i = 0; i < currentRow.length; i++)
		{
			float addend = multipleRow[i] * multiple;
			currentRow[i] = currentRow[i] + addend;
		}
		return matrix;
	}


	// Elementary row operation of swaping two rows of row1Index and row2Index (0 to 2)
	private static float[][] swapRows(float[][] matrix, int row1Index, int row2Index)
	{
		float[] rowOne = matrix[row1Index];
		float[] rowTwo = matrix[row2Index];

		matrix[row1Index] = rowTwo;
		matrix[row2Index] = rowOne;

		return matrix;
	}

	// Returns true if row contains all zeros
	private static boolean isZeroRow(float[][] matrix, int rowIndex)
	{
		for (int i = 0; i < matrix[rowIndex].length; i++)
		{
			if (matrix[rowIndex][i] != 0)
			{
				return false;
			}
		}

		return true;
	}

	// Returns true if augmented matrix is in RREF form
	private static boolean isRREF(float[][] matrix)
	{
		// Loop through each row
		for(int i = 0; i < matrix.length; i++)
		{
			// Loop through each column, except for the last one
			for (int j = 0; j < matrix[i].length - 1; j++)
			{
				// Coefficients must be 1's and 0's
				if (matrix[i][j] != 1.0 && matrix[i][j] != 0.0)
				{
					return false;
				}

				// If current element is in pivot position, check for "1"
				else if (i == j && matrix[i][j] != 1.0)
				{
					return false;
				}

				// If current element is to the left or right of pivot position, check for "0"
				else if (i != j && matrix[i][j] != 0.0)
				{
					return false;
				}

			}
		}

		// Returns true if none of the false conditions passed
		return true;
	}

	// Returns true if element fits into its current augmented matrix
	private static boolean isElementRREFCompliant(float[][] matrix, int row, int column)
	{
		if (column != matrix[row].length - 1)
		{
			if (row == column && matrix[row][column] == 1.0f)
			{
				return true;
			}

			else if (row != column && matrix[row][column] == 0.0f)
			{
				return true;
			}

			else
			{
				return false;
			}
		}

		else
		{
			return true;
		}
	}

	// Prints matrix to screen
	public static void printMatrix(float[][] matrix)
	{

		for (int i = 0; i < matrix.length; i++)
		{
			System.out.println(" ");
			for (int j = 0; j < matrix[i].length; j++)
			{
				if (j == 3)
				{
					System.out.printf("%-10s \t %-10s", "|", matrix[i][j]);
				}
				else
				{
					System.out.printf("%-10s \t", matrix[i][j]);
				}
			}
		}
	}
}