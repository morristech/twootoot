package se.amsen.par.twootoot.webcom.twitter.resource;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import se.amsen.par.twootoot.model.twitter.OAuthConfig;
import se.amsen.par.twootoot.util.annotation.UrlParameter;
import se.amsen.par.twootoot.webcom.Resource;
import se.amsen.par.twootoot.webcom.Response;
import se.amsen.par.twootoot.webcom.twitter.TwitterRequest;

/**
 * @author params on 27/10/15
 */
public class HomeTimelineResource extends Resource {
	public static class HomeTimelineReq extends TwitterRequest {
		public HomeTimelineReq(OAuthConfig oauth) {
			super(Uri.parse("https://api.twitter.com/1.1/statuses/home_timeline.json"), Method.GET, oauth);
		}

		@UrlParameter @SerializedName("count") public Integer count = 1;
		@UrlParameter @SerializedName("since_id") public Integer sinceId = 1;
		@UrlParameter @SerializedName("trim_user")  public Boolean trimUser = true;
		@UrlParameter @SerializedName("exclude_replies") public Boolean excludeReplies = true;
		@UrlParameter @SerializedName("include_entities") public Boolean includeEntities = false;
	}

	public static class HomeTimelineListResp extends Response {
		public List<HomeTimelineResp> resps;

		public HomeTimelineListResp() {
			super(CacheMode.NORMAL_CACHE);
			resps = new ArrayList<>();
		}
	}

	public static class HomeTimelineResp {
		public String idStr;
	}
}