
public class GridTile 
{
	boolean isObstacle = false;
	Character onTile = null;
	
	public void addObstacle()
	{
		isObstacle = true;
	}
	
	public boolean addCharacter( Character c )
	{
		if ( onTile != null || isObstacle )
		{
			System.out.println("Error: attempting to move player to occupied space. Tile isObstacle: " + isObstacle );
			return false;
		}
		
		onTile = c;
		return true;
	}
	
	public void removeCharacter()
	{
		onTile = null;
	}
	
	public boolean isBlocked()
	{
		return ( onTile != null || isObstacle );
	}
}
