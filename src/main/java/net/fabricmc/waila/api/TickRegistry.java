package net.fabricmc.waila.api;

import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Queues;

public class TickRegistry
{

    /**
     * We register our delegate here
     */

    public static class TickQueueElement implements Comparable<TickQueueElement>
    {
        public TickQueueElement(IScheduledTickHandler ticker, long tickCounter)
        {
            this.ticker = ticker;
            update(tickCounter);
        }
        @Override
        public int compareTo(TickQueueElement o)
        {
            return (int)(next - o.next);
        }

        public void update(long tickCounter)
        {
            next = tickCounter + Math.max(ticker.nextTickSpacing(),1);
        }

        private long next;
        public IScheduledTickHandler ticker;

        public boolean scheduledNow(long tickCounter)
        {
            return tickCounter >= next;
        }
    }

    private static PriorityQueue<TickQueueElement> clientTickHandlers = Queues.newPriorityQueue();
    private static PriorityQueue<TickQueueElement> serverTickHandlers = Queues.newPriorityQueue();

    private static AtomicLong clientTickCounter = new AtomicLong();
    private static AtomicLong serverTickCounter = new AtomicLong();

    public static void registerScheduledTickHandler(IScheduledTickHandler handler, Side side)
    {
        getQueue(side).add(new TickQueueElement(handler, getCounter(side).get()));
    }

    /**
     * @param side the side to get the tick queue for
     * @return the queue for the effective side
     */
    private static PriorityQueue<TickQueueElement> getQueue(Side side)
    {
        return side.isClient() ? clientTickHandlers : serverTickHandlers;
    }

    private static AtomicLong getCounter(Side side)
    {
        return side.isClient() ? clientTickCounter : serverTickCounter;
    }
    public static void registerTickHandler(ITickHandler handler, Side side)
    {
        registerScheduledTickHandler(new SingleIntervalHandler(handler), side);
    }

    public static void updateTickQueue(List<IScheduledTickHandler> ticks, Side side)
    {
        synchronized (ticks)
        {
            ticks.clear();
            long tick = getCounter(side).incrementAndGet();
            PriorityQueue<TickQueueElement> tickHandlers = getQueue(side);

            while (true)
            {
                if (tickHandlers.size()==0 || !tickHandlers.peek().scheduledNow(tick))
                {
                    break;
                }
                TickRegistry.TickQueueElement tickQueueElement  = tickHandlers.poll();
                tickQueueElement.update(tick);
                tickHandlers.offer(tickQueueElement);
                ticks.add(tickQueueElement.ticker);
            }
        }
    }

}

