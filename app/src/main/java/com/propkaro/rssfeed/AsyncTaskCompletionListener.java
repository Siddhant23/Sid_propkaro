package com.propkaro.rssfeed;

import java.util.List;

public interface AsyncTaskCompletionListener {
	public void onTaskComplete(List<RssFeedStructure> result);
}
