import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Character 
{
	int x;
	int y;
	long waitTime;
	
	ArrayList<MoveDirection> movesToTarget;
	

	boolean skipTurn = false;
	
	Character( int _x, int _y, long _waitTime )
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
	
	public void move()
	{
		if( movesToTarget.size() > 0 )
		{
			MoveDirection move = GridGame.fastRemove( 0, movesToTarget );
			
			if( clipMove( move ) )
			{
				move();
				return;
			}
			
			switch( move )
			{
				case N:
					y += 1;
					break;
				case S: 
					y -= 1;
					break;
				case E: 
					x += 1;
					break;
				case W:
					x -= 1; 
					break;
			}
			
			try {
				Thread.sleep( waitTime );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else
		{
			enqueueMovesToNewTarget();
			move();
		}
		
	}
	
	public boolean clipMove( MoveDirection dir )
	{
		switch( dir )
		{
			case N:
				return ( y == ( GridGame.grid.length - 1 ) );
			case S:
				return ( y == 0 );
			case E: 
				return ( x == ( GridGame.grid.length - 1 ) );
			case W: 
				return ( x == 0 );
			default:
				return true;
		}
	}
}
