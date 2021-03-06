package se.amsen.par.twootoot.source;

import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import se.amsen.par.twootoot.source.result.Result;
import se.amsen.par.twootoot.source.result.Success;
import se.amsen.par.twootoot.util.functional.AsyncRunner;
import se.amsen.par.twootoot.util.functional.Callback;
import se.amsen.par.twootoot.util.functional.Func;
import se.amsen.par.twootoot.util.functional.Func1;

/**
 * AbstractSource is now rewritten and much more functional. A Source acts as the layer
 * below the M in MVC, it provides the Model and is the entry for all CRUD operations on data.
 * Whoever wants a Model calls the appropriate Source, it is up to the Source to ex. get the Model
 * from internet, cache etc - callers should not care.
 *
 * AbstractSource also allows for async versions of all operations. The AbstractSource only enforce
 * the R & D in CRUD, it is up to each Source impl to provide other operations such as C or U etc.
 *
 * Look at OAuthSource for a concrete implementation.
 *
 * If more than 1 param is needed, use wrappers.
 *
 * @author params on 25/10/15
 */
public abstract class AbstractSource<Param1, Result1> {
	/**
	 * Reset the AbstractSource. Ex. if the AbstractSource keeps a cache it should clear it.
	 *
	 * @return true if invalidation was successful
	 */
	public boolean invalidate() {
		throw new RuntimeException("Not supported");
	}

	public AsyncRunner invalidateAsync(Callback<Result<Boolean>> callback, @Nullable TimeUnit unit, @Nullable Integer timeout) {
		return new AsyncRunner<Void, Boolean>().exec(new Func<Result<Boolean>>() {
			@Override
			public Result<Boolean> doFunc() {
				return new Success<>(invalidate());
			}
		}, callback, unit, timeout);
	}

	protected Func1<Param1, Result<Result1>> getFunc1() {
		throw new RuntimeException("Not supported");
	}

	protected AsyncRunner asyncGetResult1(Param1 p1, Callback<Result<Result1>> onCompleteCallback, @Nullable TimeUnit unit, @Nullable Integer timeout) {
		return new AsyncRunner<Param1, Result1>().exec1(p1, getFunc1(), onCompleteCallback, unit, timeout);
	}
}
