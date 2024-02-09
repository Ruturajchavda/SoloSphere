package ca.event.solosphere.ui.interfaces;


public interface RecyclerViewItemInterface {

    void OnItemClick(int position);

    void OnItemClick(int position, Object o);

    void OnItemMoved(int position, Object o);

    void OnItemShare(int position, Object o);


}
