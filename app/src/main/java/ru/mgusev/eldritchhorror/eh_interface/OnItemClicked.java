package ru.mgusev.eldritchhorror.eh_interface;

public interface OnItemClicked {
    void onItemClick(int position);
    void onEditClick(int position);
    void onDeleteClick(int position);
}
