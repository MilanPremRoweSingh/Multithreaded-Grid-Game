
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GridGame
{
	final static int gridLength = 30;
	static AtomicInteger count = new AtomicInteger( 0 );
	public static GridTile[][] grid = null;
	public static int n,p,r,k;
	public static Character[] characters;
	
    public static void main(String[] args)  
    {	
		//Ensure parameters are valid /////////////////////////////////////////////////////////////////////
    	try
		{
    		
			n = Integer.parseInt( args[0] );
			p = Integer.parseInt( args[1] );
			r = Integer.parseInt( args[2] );
			k = Integer.parseInt( args[2] );
			
			if (	n > gridLength * 4 - 2 ||  	//Max players == #tiles on perimeter
					p <= 0 ||	//At least 1 thread in pool
					r < 0 || r > ( gridLength-2 )*( gridLength-2 ) || //No more than that many objects in perimeter
					k <= 0 )
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
		//Ensure parameters are valid - end////////////////////////////////////////////////////////////////

    	//Populate grid arrays with tile objects //////////////////////////////
        grid = new GridTile[30][];
        for( int i = 0; i < grid.length; i++ )
        {
        	grid[i] = new GridTile[30];
        	for ( int j = 0; j < grid.length; j++ )
        		grid[i][j] = new GridTile();
        }
    	//Populate grid arrays with tile objects - END ////////////////////////
    	
    	Random rand = new Random();
    	
    	// Geneerate Obstacles //////////////////////////////////////
    	// Note: Very slow for very dense maps
    	for( int i = 0; i < r; i++ )
    	{
    		int x = rand.nextInt(26) + 2;
    		int y = rand.nextInt(26) + 2;
    		
    		if( grid[x][y].isBlocked() )
    			i--;
    		else
    			grid[x][y].addObstacle();
    	}
    	// Geneerate Obstacles - END ////////////////////////////////
    	
    	// Generate and place characters /////////////////////////////////////////////////
    	characters = new Character[n];
    	ArrayList<Integer> positions = new ArrayList<Integer>( n );
    	for ( int i = 0; i < gridLength * 4 - 2; i++ )
    	{
    		positions.add(i); //Create an array of the possible positions so that we never pick the same position twice
    	}
    	
    	for( int i = 0; i < n; i++ )
    	{
    		int posCode = fastRemove( rand.nextInt( positions.size() ), positions );
    		
    		int x, y;
    		if( posCode < gridLength )
    		{
    			x = posCode;
    			y = 0;
    		}
    		else if ( posCode < gridLength * 3 - 2 )
    		{
    			posCode -= gridLength;
    			x = ( posCode % 2 == 0 ) ? 0 : gridLength - 1;
    			y = posCode / 2 + 1;
    		}
    		else
    		{
    			posCode -= (2 * gridLength) + 2;
    			x = posCode;
    			y = gridLength - 1;
    		}
    		
    		long waitTime = (long)( k * (rand.nextInt(4) + 1) );
    		characters[i] = new Character( x, y, waitTime );
    	}
    	// Generate and place characters - END ///////////////////////////////////////////

    	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool( 2 ); //initialise pool
    	
    	Character test0 = new Character( 0 , 0, 1 );
    	Character test1 = new Character( 1 , 1, 1 );
    	long time = System.currentTimeMillis();
    	//for( int i = 0; i < 1000; i ++)
    	while( System.currentTimeMillis() - time < 120000)
    	{
    		if( pool.getQueue().size() < 25 )
    		{
    			
	    		pool.submit( ()-> { 
	    			test0.move(); 
	    			count.incrementAndGet();
	        		//System.out.println( "T0: (" + test0.x + ", " + test0.y + ")" );
	        		
	        		
	        		if( test0.x < 0 || test0.x > 29 )
	        		{
	        			System.out.println( "X Error " );
	        		}
	        		if( test0.y < 0 || test0.y > 29 )
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
	        		//System.out.println( "T0: (" + test0.x + ", " + test0.y + ")" );
	    		} );
	    		
	    		pool.submit( ()-> { 
		    		test1.move();
	    			count.incrementAndGet();
		    		
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
	
	        		//System.out.println( "T1: (" + test1.x + ", " + test1.y + ")" );
	    		});
    		}	
    	}
    	pool.shutdownNow();
    	
    	/*
    	System.out.println( "Done submitting to pool");
    	try {
			while( !pool.awaitTermination( 1, TimeUnit.SECONDS ) )
				System.out.println("waititng");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
    
    	System.out.println( count.get() + " moves, done in " + ( System.currentTimeMillis() - time )/1000 +"s" );
    	
        
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