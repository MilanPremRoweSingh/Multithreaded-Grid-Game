import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Character 
{
	int x;
	int y;
	long speed;
	
	ArrayList<MoveDirection> movesToTarget;
	

	boolean skipTurn = false;
	
	Character( int _x, int _y, long _speed )
	{
		movesToTarget = new ArrayList<MoveDirection>( 8 );//Target is within 8 moves
	}
	
	public void enqueueMovesToNewTarget()
	{
		int moveIdx = ThreadLocalRandom.current().nextInt(4); //Generate one move to ensure some movement is attempted
		movesToTarget.add( MoveDirection.values()[ moveIdx ] );
		
		for( int i = 0; i < 7; i++ )
		{
			moveIdx = ThreadLocalRandom.current().nextInt(5);
			if( moveIdx < 4 )
			{
				movesToTarget.add( MoveDirection.values()[ moveIdx ] );
			}
		}
	}
}
