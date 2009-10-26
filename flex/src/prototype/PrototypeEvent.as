package prototype {
import flash.events.Event;

public class PrototypeEvent extends Event {

    public static const NAVIGATION:String = "navigation";

    public var state:String;

    public function PrototypeEvent(state:String) {
        super(NAVIGATION);
        this.state = state;
    }

    override public function clone():Event {
        return new PrototypeEvent(state);
    }
}
}