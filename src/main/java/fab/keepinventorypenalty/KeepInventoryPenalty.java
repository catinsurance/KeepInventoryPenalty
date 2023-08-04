package fab.keepinventorypenalty;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeepInventoryPenalty implements ModInitializer
{
	public static String ID = "keep-inventory-penalty";
    public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("KeepInventoryPenalty initialized.");
	}
}