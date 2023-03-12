package com.autovend.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.devices.BillStorage;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillStorageObserver;

@SuppressWarnings("javadoc")
public class BillStorageTest {
	private BillStorage storage;
	private Currency currency;
	private Bill bill;
	private int found;

	@Before
	public void setup() {
		storage = new BillStorage(1);
		currency = Currency.getInstance(Locale.CANADA);
		bill = new Bill(1, currency);
		found = 0;
	}

	@Test(expected = SimulationException.class)
	public void testAcceptNull() throws OverloadException, DisabledException {
		storage.accept(null);
	}

	@Test(expected = SimulationException.class)
	public void testCreateZeroCapacity() {
		new BillStorage(0);
	}

	@Test
	public void testCapacityAndCount() {
		assertEquals(1, storage.getCapacity());
		assertEquals(0, storage.getBillCount());
	}

	@Test(expected = SimulationException.class)
	public void testLoadNullBillArray() throws SimulationException, OverloadException {
		storage.load((Bill[])null);
	}

	@Test(expected = SimulationException.class)
	public void testLoadNullBill() throws SimulationException, OverloadException {
		storage.load((Bill)null);
	}

	@Test(expected = SimulationException.class)
	public void testLoadNullBillInArray() throws SimulationException, OverloadException {
		storage.load(new Bill[] { null });
	}

	@Test(expected = OverloadException.class)
	public void testOverload() throws SimulationException, OverloadException {
		storage.load(bill, bill);
	}

	@Test
	public void testLoad() throws SimulationException, OverloadException {
		storage.register(new BillStorageObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToBillsUnloadedEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillsLoadedEvent(BillStorage unit) {
				found++;
			}

			@Override
			public void reactToBillsFullEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillAddedEvent(BillStorage unit) {
				fail();
			}
		});
		storage.load(bill);
		assertEquals(1, found);
	}

	@Test
	public void testUnloadWhenEmpty() {
		storage.register(new BillStorageObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToBillsUnloadedEvent(BillStorage unit) {
				found++;
			}

			@Override
			public void reactToBillsLoadedEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillsFullEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillAddedEvent(BillStorage unit) {
				fail();
			}
		});

		List<Bill> result = storage.unload();
		assertEquals(0, result.size());
	}

	@Test(expected = DisabledException.class)
	public void testDisabledAccept() throws DisabledException, OverloadException {
		storage.disable();
		storage.accept(null);
	}

	@Test
	public void testHasSpace() {
		assertEquals(true, storage.hasSpace());
	}
	
	@Test
	public void testHasSpaceWhenFull() throws DisabledException, OverloadException {
		storage.accept(bill);
		assertEquals(false, storage.hasSpace());
	}
	
	@Test
	public void testAccept() throws DisabledException, OverloadException {
		storage.register(new BillStorageObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToBillsUnloadedEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillsLoadedEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillsFullEvent(BillStorage unit) {
				found++;
			}

			@Override
			public void reactToBillAddedEvent(BillStorage unit) {
				fail();
			}
		});
		storage.accept(bill);
		assertEquals(1, storage.getBillCount());
		assertEquals(1, found);
	}

	@Test
	public void testAcceptWithoutFilling() throws DisabledException, OverloadException {
		storage = new BillStorage(2);
		storage.register(new BillStorageObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToBillsUnloadedEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillsLoadedEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillsFullEvent(BillStorage unit) {
				fail();
			}

			@Override
			public void reactToBillAddedEvent(BillStorage unit) {
				found++;
			}
		});
		storage.accept(bill);
		assertEquals(1, storage.getBillCount());
		assertEquals(1, found);
	}

	@Test(expected = OverloadException.class)
	public void testOverloadAccept() throws DisabledException, OverloadException {
		storage.accept(bill);
		storage.accept(bill);
	}
}
