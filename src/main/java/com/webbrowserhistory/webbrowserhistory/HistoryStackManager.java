package com.webbrowserhistory.webbrowserhistory;

import java.util.LinkedList;

public class HistoryStackManager {
    private LinkedList<WebsiteHistory> backStack;
    private LinkedList<WebsiteHistory> forwardStack;
    private LinkedList<WebsiteHistory> history;

    public HistoryStackManager(){
        backStack = new LinkedList<>();
        forwardStack = new LinkedList<>();
        history = new LinkedList<>();
    }

    public void visit(WebsiteHistory site){
        backStack.push(site);
        history.push(site);
    }

    public WebsiteHistory peekIndex(int i){
        // If the requested index is within the bounds of the stack, return the item
        if (i<history.size() && i>=0) {
            return history.get(i);
        }
        return null;
    }

    public boolean remove(WebsiteHistory site){
        return history.remove(site);
    }

    public WebsiteHistory back(){
        if (!backStack.isEmpty()) {
            WebsiteHistory backSite = backStack.pop();
            forwardStack.push(backSite);
            return backStack.peek();
        }
        return null;
    }

    public WebsiteHistory forward(){
        if (!forwardStack.isEmpty()){
            WebsiteHistory forwardSite = forwardStack.pop();
            backStack.push(forwardSite);
            return backStack.peek();
        }
        return null;
    }

    public int getHistorySize() {
        return history.size();
    }

    public void deleteHistory(){
        if (!backStack.isEmpty()){
            backStack.clear();
        }
        if (!forwardStack.isEmpty()){
            forwardStack.clear();
        }
        if (!history.isEmpty()){
            history.clear();
        }
    }

    @Override
    public String toString(){
        String s = "backStack: " + backStack.toString() + "\n" + "forwardStack: " + forwardStack.toString()
                + "\n" + "history: " + history.toString();
        return s;

    }
}
