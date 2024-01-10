package mcp.mobius.waila.api;

public interface IWailaRegistrar {

    void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);

    void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);

    void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, int paramInt);

    void registerStackProvider(IWailaDataProvider paramIWailaDataProvider, Class<?> paramClass);

    void registerHeadProvider(IWailaDataProvider paramIWailaDataProvider, Class<?> paramClass);

    void registerBodyProvider(IWailaDataProvider paramIWailaDataProvider, Class<?> paramClass);

    void registerDocTextFile(String paramString);

    void registerShortDataProvider(IWailaSummaryProvider paramIWailaSummaryProvider, Class<?> paramClass);
}
