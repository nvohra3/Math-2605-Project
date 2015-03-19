/**
 * Matrix class can be used to represent a matrix or vector as
 * a two dimensional array and supports various operations
 */
public class Matrix {
	private double[][] matrix;
	private final int numRows;
	private final int numCols;

	public Matrix(int rows, int cols) {
		matrix = new double[rows][cols];
		numRows = rows;
		numCols = cols;
	}

	public Matrix(double[][] array) {
		this.matrix = array;
		numRows = matrix.length;
		numCols = matrix[0].length;
	}

	/**
	 * @return matrix result of adding two matrices together
	 */
	public Matrix add(Matrix m) {
		if (!haveEqualDimensions(m))
			throw new IllegalArgumentException("Matrices cannot be added.");

		Matrix result = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result.matrix[i][j] = this.matrix[i][j] + m.matrix[i][j];
		return result;
	}

	/**
	 * @return matrix result of subtracting two matrices
	 */
	public Matrix subtract(Matrix m) {
		if (!haveEqualDimensions(m))
			throw new IllegalArgumentException("Matrices cannot be subtracted.");

		Matrix result = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result.matrix[i][j] = this.matrix[i][j] - m.matrix[i][j];
		return result;
	}

	/**
	 * @param m matrix to multiply by
	 * @return matrix result of multiplying two matrices
	 */
	public Matrix multiply(Matrix m) {
		if (!checkDims(m))
			throw new IllegalArgumentException("Matrices cannot be multiplied.");

		Matrix result = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < m.numCols; j++)
				for (int k = 0; k < numCols; k++)
					result.matrix[i][j] += matrix[i][k] * m.matrix[k][j];
		return result;
	}

	/**
	 * @param scalar number to multiply every entry in matrix by
	 * @return matrix result of scalar times matrix
	 */
	public Matrix multiply(double scalar) {
		Matrix result = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result.matrix[i][j] = matrix[i][j] * scalar;
		return result;
	}

	/**
	 * @return dot product of two vectors
	 */
	public Matrix dotProduct(Matrix m) { return multiply(m); }

	/**
	 * @return transpose of matrix
	 */
	public Matrix transpose() {
		Matrix result = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			for (int j = 0; j < numCols; j++)
				result.matrix[j][i] = matrix[i][j];
		return result;
	}

    /**
     * @return diagonal of matrix
     */
    public Matrix diagonalize(){
		Matrix result = new Matrix(numRows, numCols);
        for(int i = 0; i < numRows; i++)
			result.matrix[i][i] = matrix[i][i];
        return result;
    }

    /**
     * @return matrix containing absolute values of each entry in original matrix
     */
    public Matrix absoluteValue() {
		Matrix result = new Matrix(numRows, numCols);
        for(int i = 0; i < numRows; i++)
			for(int j = 0; j < numCols; j++)
				result.matrix[i][j] = Math.abs(matrix[i][j]);
        return result;
    }

	/**
	 * Performs LU decomposition of matrix
	 * Worked out examples of how to calculate LU decomposition are at the following link
	 * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/examples_LU_Householder.pdf
	 * @return matrices l and u
	 */
	public Matrix[] lu_fact() {
		// Can LU factorization happen on non-square matrices? Account for this at some point.
		Matrix l = getIdentityMatrix(numRows);
		// Copy original matrix into u for row reduction and to avoid changing original matrix
		Matrix u = new Matrix(numRows, numCols);
		for (int i = 0; i < numRows; i++)
			System.arraycopy(matrix[i], 0, u.matrix[i], 0, numCols);

		for (int j = 0; j < numCols - 1; j++)
			for (int i = j + 1; i < numRows; i++)
				if (u.matrix[i][j] != 0)
				{
					double scalar = u.matrix[i][j]/u.matrix[j][j];
					// Putting scalar in l because this is equivalent to the inverse of the matrix G_n at each step
					l.matrix[i][j] = scalar;
					u.rowOperation(j, i, scalar);
				}
		Matrix[] list = new Matrix[2];
		list[0] = l;
		list[1] = u;
		return list;
	}

	/**
	 * Solves Ax = b where A = LU
	 * @param b matrix used when solving for x
	 * @return matrix solution of system
	 */
	public Matrix solve_lu_b(Matrix b) {
		Matrix[] list = this.lu_fact();
		// Solve Ly = b using forward substitution since L is an lower triangular matrix
		Matrix y = forwardSubstitution(list[0], b);
		// Solve Ux = y using backwards substitution since U is a upper triangular matrix
		Matrix x = backwardSubstitution(list[1], y);
		return x;
	}

	/**
	 * Solves equation Ax = b
	 * @param a upper triangular, n by n matrix
	 * @param b n by 1 vector
	 * @return matrix solution of system
	 */
	private Matrix forwardSubstitution(Matrix a, Matrix b) {
		Matrix x = new Matrix(a.numCols, 1);
		double total;
		for (int i = 0; i < a.numCols; i++)
		{
			total = 0;
			for (int j = 0; j < i; j++)
				total += a.matrix[i][j] * x.matrix[j][0];
			double x_n = (b.matrix[i][0] - total) / a.matrix[i][i];
			x.matrix[i][0] = x_n;
		}
		return x;
	}

	private Matrix backwardSubstitution(Matrix a, Matrix b) {
		Matrix x = new Matrix(a.numCols, 1);
		double total;
		for (int i = a.numCols - 1; i >= 0; i--)
		{
			total = 0;
			for (int j = a.numCols - 1; j > i; j--)
				total += a.matrix[i][j] * x.matrix[j][0];
			double x_n = (b.matrix[i][0] - total) / a.matrix[i][i];
			x.matrix[i][0] = x_n;
		}
		return x;
	}

	/**
	 * Performs a QR factorization of a square matrix using Givens Rotations where
	 * Q = (G_1)^t * (G_2)^t * (G_m)^t where ^t indicates a transpose of a matrix
	 * R = G_m * ... * G_2 * G_1 * A
	 * Worked out example of how to perform this calculation is on page 9 of the following link
	 * https://files.t-square.gatech.edu/access/content/group/gtc-1e04-e0d7-51e4-a454-b328339e73da/2605classnotesWeek6_b.pdf
	 * @return matrices Q and R
	 */
	public Matrix[] qr_fact_givens() {
		// Can QR factorization using Givens be done on non-square matrices?
		Matrix q = null;
		// Copy original matrix into r for row reduction and to avoid changing original matrix
		Matrix r = new Matrix(matrix);
		for (int j = 0; j < numCols; j++)
			for (int i = j + 1; i < numRows; i++)
				if (r.matrix[i][j] != 0)
				{
					double x = r.matrix[j][j];
					double y = r.matrix[i][j];
					// cos theta = x/sqrt(x^2 + y^2)
					double cosTheta = x / (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
					// sin theta = -y/sqrt(x^2 + y^2)
					double sinTheta = -y / (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
					Matrix g = getIdentityMatrix(numRows);
					g.matrix[i][i] = cosTheta;
					g.matrix[j][j] = cosTheta;
					g.matrix[i][j] = sinTheta;
					g.matrix[j][i] = -sinTheta;
					r = g.multiply(r);
					// Sets q equal to "G_1" the first time the loop runs, otherwise calculates q as it should
					if (q == null)
						q = g.transpose();
					else
						q = q.multiply(g.transpose());
				}
		Matrix[] list = new Matrix[2];
		list[0] = q;
		list[1] = r;
		return list;
	}

	/**
	 * A Hilbert matrix is a square matrix whose entries are defined as H_ij = 1 / (i + j - 1)
	 * @param dim dimension of Hilbert matrix
	 * @return Hilbert matrix represented a 2D array
	 */
	public static Matrix getHilbertMatrix(int dim) {
		Matrix hilbert = new Matrix(dim, dim);
		for (int i = 0; i < dim; i++)
			for (int j = 0; j < dim; j++)
				// Simply adding 1 since arrays are 0 indexed
				hilbert.matrix[i][j] = (double) 1 / (i + j + 1);
		return hilbert;
	}

	/**
	 * An identity matrix is defined as a matrix with n rows and columns with a diagonal of 1s
	 * Method returns a two dimensional representation of the array
	 * @param dim dimensions of array
	 * @return identity array
	 */
	public static Matrix getIdentityMatrix(int dim) {
		Matrix identity = new Matrix(dim, dim);
		for (int i = 0; i < dim; i++)
			identity.matrix[i][i] = 1;
		return identity;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numRows; i++)
		{
			for (int j = 0; j < numCols; j++)
				sb.append(matrix[i][j] + "\t\t\t");

			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Performs row operation on array based on parameters
	 * @param array array on which row operation is being performed
	 * @param pivot pivot row of array
	 * @param row row whose values are being changed
	 * @param scalar number to multiply the pivot row by and add to specified row
	 * @return result of row operation
	 */
	private void rowOperation(int pivot, int row, double scalar) {
		for (int j = 0; j < matrix[0].length; j++)
			matrix[row][j] += (matrix[pivot][j] * scalar * -1);
	}

	/**
	 * @return true if two matrices have an equal number of rows and columns
	 */
	private boolean haveEqualDimensions(Matrix m) {
		return ((this.numRows == m.numRows) && (this.numCols == m.numCols));
	}

	/**
	 * @return true if number of rows in A is equal to number of columns in B
	 */
	private boolean checkDims(Matrix m) {
		return (this.numCols == m.numRows);
	}
}