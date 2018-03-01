package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

import tetris.model.Piece;
import tetris.model.PieceType;

class RotationPieceTest {

	@Test
	public void rotateRightLTest() {
		Point[] points = new Point[4];
		Piece piece = Piece.getPiece(PieceType.L);
		points[0] = new Point(0, 0);
		points[1] = new Point(1, 0);
		points[2] = new Point(-1, 0);
		points[3] = new Point(-1, -1);
		piece.rotate();
		assertTrue(correspondancePoints(points, piece.getPoints()));

		
		Point[] points2 = new Point[4];
		points2[0] = new Point(0, 1);
        points2[1] = new Point(0, 0);
        points2[2] = new Point(0, -1);
        points2[3] = new Point(1, -1);
		piece.rotate();
		assertTrue(correspondancePoints(points, piece.getPoints()));
		
		Point[] points3 = new Point[4];
	    points3[0] = new Point(-1, 0);
        points3[1] = new Point(0, 0);
        points3[2] = new Point(1, 0);
        points3[3] = new Point(1, 1);
        piece.rotate();
		assertTrue(correspondancePoints(points, piece.getPoints())); 

	}
	
	

	public Boolean correspondancePoints(Point[] points, Point[] piece) {
		int count = 0;

		for (Point point : points) {
			for (Point pieces : piece) {
				if (point.x == pieces.x && point.y == pieces.y) {
					count++;
				}
			}

		}
		if (count == 4) {
			return true;
		} else {
			return false;
		}

	}

}
