
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GridGame
{
	static AtomicInteger count = new AtomicInteger( 0 );
	public static boolean[][] grid = null;
	public static int n,p,r,k;
	
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

    	ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool( 2 );
    	
    	long time = System.currentTimeMillis();
    	int locCount = 0;
    	
    	while( System.currentTimeMillis() - time < 100l )
    	{
    		pool.submit( () -> {
    			System.out.println( "Execution: " + count.getAndIncrement() );
    		});
    		locCount++;
    	}
    	
    	try {
			pool.awaitTermination( 100, TimeUnit.SECONDS );
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println( locCount );
    	
        grid = new boolean[30][];
        for( int i = 0; i < grid.length; i++ )
        	grid[i] = new boolean[30];
    	
        grid[0][0] 	= true;
        grid[0][2] 	= true;
        grid[1][2] 	= true;
        grid[23][4] = true; 
        grid[8][1] 	= true;
        grid[9][21] = true;
        grid[29][29] = true;
        
        GameWindow window = new GameWindow( grid, 780, 780 );
    	
    }

   


}