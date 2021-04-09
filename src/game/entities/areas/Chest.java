package game.entities.areas;
import game.entities.players.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static game.Level.getPlayers;

public class Chest extends AnimArea{

	public Chest(float x, float y, int width, int height, String...urls) {
		super(x, y, width, height,urls);
	}

	public void tick() {

		for (Player p: getPlayers())
		{
			if (this.isVisibile())
			{
				if (this.getInteraction(p))
				{
					if (p.firstFreeSpace()!=-1)
					{
						p.getInventory().get(p.firstFreeSpace()).setUrl(randomItem());
						this.setVisibile(false);
						p.setInventoryChanged(true);

					}
				}
			}
		}
	}


	public String randomItem(){
		ArrayList<String> itemPool =new ArrayList<String>(Arrays.asList("./img/shoes.png","./img/jump.png","./img/banana.png"));
		int rnd1;
		rnd1 = new Random().nextInt(itemPool.size());

		return itemPool.get(rnd1);
	}

}