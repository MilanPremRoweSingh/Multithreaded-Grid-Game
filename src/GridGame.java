
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class GridGame
{
	static AtomicInteger count = new AtomicInteger( 0 );
	public static GridTile[][] grid = null;
	public static int n,p,r,k;
	public static Character[] characters;
	
    public static void main(String[] args)  
    {
    	try
		{
    		final int gridLength = 30;
    		
			n = Integer.parseInt( args[0] );
			p = Integer.parseInt( args[1] );
			r = Integer.parseInt( args[2] );
			k = Integer.parseInt( args[2] );
			
			if ( n > gridLength * 4 - 2 || p <= 0 || r < 0 ||r > gridLength*gridLength-n || k <= 0 )
			{
				System.out.println( "Invalid arguments" );
				return;
			}
		}
		catch ( NumberFormatException exc )
		{
			System.out.println( "Invalid arguments" );
			return;
		}
		catch ( IndexOutOfBoundsException exc )
		{
			System.out.println( "Invalid arguments" );
			return;
		}

        grid = new GridTile[30][];
        for( int i = 0; i < grid.length; i++ )
        {
        	grid[i] = new GridTile[30];
        	for ( int j = 0; j < grid.length; j++ )
        		grid[i][j] = new GridTile();
        }
    	
    	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool( 2 );
    	
    	Random rand = new Random();
    	for( int i = 0; i < r; i++ )
    	{
    		int x = rand.nextInt(28) + 2;
    		int y = rand.nextInt(28) + 2;
    		
    		if( grid[x][y].isBlocked() )
    			i--;
    		else
    			grid[x][y].addObstacle();
    	}
    	
    	
    	Character test0 = new Character( 0 , 0, 1 );
    	Character test1 = new Character( 1 , 1, 1 );
    	while( true )
    	{
    		pool.submit( ()-> { 
    			test0.move(); 
        		//System.out.println( "T0: (" + test0.x + ", " + test0.y + ")" );
        		
        		
        		if( test0.x < 0 || test0.x > 29 )
        		{
        			System.out.println( "X Error " );
        		}
        		if( test0.y < 0 || test0.y > 29 )
        		{
        			System.out.println( "X Error " );
        		}

        		//System.out.println( "T0: (" + test0.x + ", " + test0.y + ")" );
    		} );
    		
    		pool.submit( ()-> { 
	    		test1.move();
	    		
	    		//System.out.println( "T1: (" + test1.x + ", " + test1.y + ")" );
	    		
	    		
	    		if( test1.x < 0 || test1.x > 29 )
	    		{
	    			System.out.println( "X Error " );
	    			
	    		}
	    		if( test1.y < 0 || test1.y > 29 )
	    		{
	    			System.out.println( "X Error " );
	    			
	    		}
	    		
	    		if( test0.x == test1.x && test0.y == test1.y )
	    		{
	    			System.out.println( "Character Overlap error" );
	    			
	    		}
	    		
	    		if( grid[ test0.x ][ test0.y ].isObstacle || grid[ test1.x ][ test1.y ].isObstacle  )
	    		{
	    			System.out.println( "Obstacle Overlap error" );
	    			
	    		}
    		} );
    			
    	}
    	
    	/*
    	long time = System.currentTimeMillis();
    	int locCount = 0;
    	
    	while( System.currentTimeMillis() - time < 100l ) //Game loop 
    	{
    		pool.submit( () -> {
    			System.out.println( "Execution: " + count.getAndIncrement() );
    		}); //TEST
    		locCount++;
    	}
    	
    	try {
			pool.awaitTermination( 120, TimeUnit.SECONDS ); //Join Threads
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println( locCount ); //TEST
    	*/
        
        //GameWindow window = new GameWindow( grid, 780, 780 );
    	
    }
    
    public static <T> T fastRemove( int idx, ArrayList<T> array )
	{
		T temp = array.get( idx );
		
		array.set( idx, array.get( array.size() - 1 ) );
		array.remove( array.size() - 1 );
		
		return temp;
	}
   


}