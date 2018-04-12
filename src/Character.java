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
		x = _x;
		y = _y;
		GridGame.grid[y][x].addCharacter( this );
		waitTime = _waitTime;
		movesToTarget = new ArrayList<MoveDirection>( 8 );//Target is within 8 moves
	}
	
	public void enqueueMovesToNewTarget()
	{
		movesToTarget.clear();
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
	
	public void enqueueMovesToNewTargetFixed()
	{
		movesToTarget.clear();
		boolean validTargetPicked = false;
		
		int rX = 0;
		int rY = 0;
		while( !validTargetPicked )
		{
			rX = ThreadLocalRandom.current().nextInt(16) - 8 + 1;
			rY = ThreadLocalRandom.current().nextInt( (8-Math.abs( rX ))*2 ) - rX/2 +1;
			rX += x;
			rY += y;
			
			if( rX < 0 || rX > GridGame.grid.length - 1 )
				continue;
			
			if( rY < 0 || rY > GridGame.grid.length - 1 )
				continue;
			
			if( GridGame.grid[rX][rY].isBlocked() )
				continue;
			
			validTargetPicked = true;
		}
		
		while( rX != 0 && rY != 0 )
		{
			boolean moveRight = ThreadLocalRandom.current().nextBoolean();
			
			if( moveRight && rX != 0 )
			{
				if( rX < 0 )
				{
					movesToTarget.add( MoveDirection.W );
					rX++;
				}
				else
				{
					movesToTarget.add( MoveDirection.E );
					rX--;
				}
			}
			else
			{
				if( rY < 0 )
				{
					movesToTarget.add( MoveDirection.S );
					rY++;
				}
				else
				{
					movesToTarget.add( MoveDirection.N );
					rY--;
				}
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
			boolean genNewQueue = false;
			switch( move )
			{
				case N:
					synchronized( GridGame.grid[x][y] )
					{
						synchronized( GridGame.grid[x][y+1] )
						{
							if( !GridGame.grid[x][y+1].isBlocked() )
							{
								GridGame.grid[x][y].removeCharacter();
								y += 1;
								GridGame.grid[x][y].addCharacter( this );
							} 
							else
								genNewQueue = true;
						}
					}
					break;
				case S: 
					synchronized( GridGame.grid[x][y-1] )
					{
						synchronized( GridGame.grid[x][y] )
						{
							if( !GridGame.grid[x][y-1].isBlocked() )
							{
								GridGame.grid[x][y].removeCharacter();
								y -= 1;
								GridGame.grid[x][y].addCharacter( this );
							} 
							else
								genNewQueue = true;
						}
					}
					break;
				case E: 
					synchronized( GridGame.grid[x][y] )
					{
						synchronized( GridGame.grid[x+1][y] )
						{
							if( !GridGame.grid[x+1][y].isBlocked() )
							{
								GridGame.grid[x][y].removeCharacter();
								x += 1;
								GridGame.grid[x][y].addCharacter( this );
							} 
							else
								genNewQueue = true;
						}
					}
					break;
				case W:
					synchronized( GridGame.grid[x-1][y] )
					{
						synchronized( GridGame.grid[x][y] )
						{
							if( !GridGame.grid[x-1][y].isBlocked() )
							{
								GridGame.grid[x][y].removeCharacter();
								x -= 1;
								GridGame.grid[x][y].addCharacter( this );
							} 
							else
								genNewQueue = true;
						}
					}
					break;
			}
			
			if( genNewQueue )
				enqueueMovesToNewTargetFixed();
			
			try {
				Thread.sleep( waitTime );
			} catch (InterruptedException e) {
				return;
			}
		}
		else
		{
			enqueueMovesToNewTargetFixed();
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
