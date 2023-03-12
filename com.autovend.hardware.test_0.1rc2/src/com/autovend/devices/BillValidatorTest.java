package com.autovend.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.autovend.Bill;
import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.BillSlotObserver;
import com.autovend.devices.observers.BillValidatorObserver;

@SuppressWarnings("javadoc")
public class BillValidatorTest {
	private BillValidator validator;
	private Currency currency;
	private Bill bill;
	/* used to record occurrences of events */
	private int found;
	/*
	 * used to record that a test has to be retried due to unwanted, random
	 * fluctuations
	 */
	private boolean retry;

	/**
	 * Default setup: validator is initialized bills of denomination 1 Canadian
	 * dollar. This setup will be "undone" for certain test cases.
	 */
	@Before
	public void setup() {
		currency = Currency.getInstance(Locale.CANADA);
		bill = new Bill(1, currency);
		validator = new BillValidator(currency, new int[] { 1 });
		found = 0;
		retry = false;
	}

	/**
	 * Check that accepting a null bill causes a SimulationException.
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test(expected = SimulationException.class)
	public void testAcceptNullBill() throws OverloadException, DisabledException {
		validator.accept(null);
	}

	/**
	 * Check that construction with a null currency causes a SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testCreateWithNullCurrency() {
		new BillValidator(null, new int[] { 1 });
	}

	/**
	 * Check that construction with a null denominations list causes a
	 * SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testCreateWithNullDenominations() {
		new BillValidator(currency, null);
	}

	/**
	 * Check that construction with an empty denominations list causes a
	 * SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testCreateWithEmptyDenominations() {
		new BillValidator(currency, new int[0]);
	}

	/**
	 * Check that construction with a denominations list containing a negative value
	 * causes a SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testCreateWithDenominationsListContainingNegativeValue() {
		new BillValidator(currency, new int[] { -1 });
	}

	/**
	 * Check that construction with a denominations list containing identical values
	 * causes a SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testCreateWithIdenticalDenominations() {
		new BillValidator(currency, new int[] { 1, 1 });
	}

	/**
	 * Check that connecting the device to two null channels causes a
	 * SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testConnectToNulls() {
		validator.connect(null, null);
	}

	/**
	 * Check that connecting the device to a null sink causes a SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testConnectToNullSink() {
		validator.connect(new BidirectionalChannel<>(null, validator), null);
	}

	/**
	 * Check that connecting the device to a null source causes a
	 * SimulationException.
	 */
	@Test(expected = SimulationException.class)
	public void testConnectToNullSource() {
		validator.connect(null, new UnidirectionalChannel<>(null));
	}

	/**
	 * Test that accepting a valid bill causes it to announce a validBillDetected
	 * event and for it to go to storage. We need to deal with random fluctuations.
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test
	public void testAcceptValidBill() throws DisabledException, OverloadException {
		validator.register(new BillValidatorObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
				found++;
			}

			@Override
			public void reactToInvalidBillDetectedEvent(BillValidator validator) {
				// assume that invalid bills are detected due to random fluctuations, so retry
				retry = true;
			}
		});

		BidirectionalChannel<Bill> channel = new BidirectionalChannel<>(new FlowThroughEmitter<Bill>() {
			@Override
			public boolean emit(Bill thing) throws DisabledException, OverloadException {
				return true; // false rejection will happen sometimes; ignore it
			}
		}, validator);

		validator.connect(channel, new UnidirectionalChannel<>(new Acceptor<Bill>() {
			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				found++;
				return true;
			}

			@Override
			public boolean hasSpace() {
				return true;
			}
		}));

		do {
			found = 0;
			retry = false;
			validator.accept(bill);
		}
		while(retry);

		assertEquals(2, found); // valid bill + storage accepts
	}

	/**
	 * Check that a valid bill will sometimes be falsely rejected, leading to an
	 * invalidBillDetected event. Since we need to deal with random fluctuations, we
	 * need to ignore validBillDetected events and retry.
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test
	public void testAcceptFalseNegative() throws DisabledException, OverloadException {
		validator.register(new BillValidatorObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
				retry = true;
			}

			@Override
			public void reactToInvalidBillDetectedEvent(BillValidator validator) {
				found++;
			}
		});

		BillSlot slot = new BillSlot(false);
		BidirectionalChannel<Bill> channel = new BidirectionalChannel<>(slot, validator);
		slot.connect(channel);
		// We connect the validator to a stubbed sink that always claims it is full, so
		// any bills going there will be bounced back to the slot.
		validator.connect(channel, new UnidirectionalChannel<>(new Acceptor<Bill>() {
			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				return false;
			}

			@Override
			public boolean hasSpace() {
				return false;
			}
		}));

		do {
			// If this is the second try or later, we need to remove the bill that is
			// dangling from the previous try.
			slot.removeDanglingBill();
			retry = false;
			slot.accept(bill);
		}
		while(retry);

		assertEquals(1, found);
		Bill foundBill = slot.removeDanglingBill();
		assertEquals(bill, foundBill);
	}

	/**
	 * If the bill we insert is of the wrong currency, we expect there to be an
	 * invalidBillDetected event
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test
	public void testAcceptWrongCurrency() throws DisabledException, OverloadException {
		// Use an accepting source stub.
		BidirectionalChannel<Bill> channel = new BidirectionalChannel<>(new FlowThroughEmitter<Bill>() {
			@Override
			public boolean emit(Bill thing) throws DisabledException, OverloadException {
				found++;
				return true; // All good
			}
		}, validator);

		// Use a failing sink stub. This should not be called.
		validator.connect(channel, new UnidirectionalChannel<>(new Acceptor<Bill>() {
			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				fail();
				return false;
			}

			@Override
			public boolean hasSpace() {
				fail();
				return false;
			}
		}));

		// Bill in incorrect currency.
		bill = new Bill(1, Currency.getInstance(Locale.US));
		validator.register(new BillValidatorObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
				fail();
			}

			@Override
			public void reactToInvalidBillDetectedEvent(BillValidator validator) {
				found++;
			}
		});

		validator.accept(bill);
		assertEquals(2, found);
	}

	/**
	 * If the bill we insert is of the wrong denomination, we expect there to be an
	 * invalidBillDetected event.
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test
	public void testBadDenomination() throws DisabledException, OverloadException {
		// Use an accepting source stub.
		BidirectionalChannel<Bill> channel = new BidirectionalChannel<>(new FlowThroughEmitter<Bill>() {
			@Override
			public boolean emit(Bill thing) throws DisabledException, OverloadException {
				found++;
				return true; // All good
			}
		}, validator);

		// Use a failing sink stub.
		validator.connect(channel, new UnidirectionalChannel<>(new Acceptor<Bill>() {
			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				fail();
				return false;
			}

			@Override
			public boolean hasSpace() {
				fail();
				return false;
			}
		}));
		bill = new Bill(2, currency);

		validator.register(new BillValidatorObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToValidBillDetectedEvent(BillValidator validator, Currency currency, int value) {
				fail();
			}

			@Override
			public void reactToInvalidBillDetectedEvent(BillValidator validator) {
				found++;
			}
		});

		validator.accept(bill);
		assertEquals(2, found);
	}

	/**
	 * Check that accepting when the device is disabled causes a DisabledException.
	 * 
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test(expected = DisabledException.class)
	public void testDisableAccept() throws DisabledException {
		validator.disable();
		validator.accept(null);
	}

	/**
	 * Check that disabling the device when it is already disabled does not alter
	 * its disabled state.
	 */
	@Test
	public void testDisabledDisable() {
		validator.disable();
		assertTrue(validator.isDisabled());
		validator.disable();
		assertTrue(validator.isDisabled());
	}

	/**
	 * Check that enabling the device when it is already enabled does not alter its
	 * enabled state.
	 */
	@Test
	public void testEnabledEnable() {
		validator.disable();
		validator.enable();
		assertTrue(!validator.isDisabled());
		validator.enable();
		assertTrue(!validator.isDisabled());
	}

	/**
	 * When we try to accept a good bill but neither the sink nor source will take
	 * it, a SimulationException should happen.
	 * 
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test(expected = SimulationException.class)
	public void testAcceptGoodBillWithRejectingSink() throws DisabledException {
		FlowThroughEmitter<Bill> stub = new FlowThroughEmitter<Bill>() {
			@Override
			public boolean emit(Bill thing) throws DisabledException, OverloadException {
				throw new OverloadException();
			}
		};
		Acceptor<Bill> stub2 = new Acceptor<Bill>() {
			@Override
			public boolean hasSpace() {
				return false;
			}

			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				fail();
				return false;
			}
		};
		validator.connect(new BidirectionalChannel<>(stub, validator), new UnidirectionalChannel<>(stub2));
		validator.accept(bill);
	}

	/**
	 * If the sink claims to have space but then doesn't, this should result in a
	 * SimulationException.
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test(expected = SimulationException.class)
	public void testAcceptGoodBillWithLyingSink() throws DisabledException, OverloadException {
		FlowThroughEmitter<Bill> stub = new FlowThroughEmitter<Bill>() {
			@Override
			public boolean emit(Bill thing) throws DisabledException, OverloadException {
				// It will get emitted sometimes, so ignore it.
				return false;
			}
		};
		Acceptor<Bill> stub2 = new Acceptor<Bill>() {
			@Override
			public boolean hasSpace() {
				return true;
			}

			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				throw new OverloadException();
			}
		};
		validator.connect(new BidirectionalChannel<>(stub, validator), new UnidirectionalChannel<>(stub2));

		for(int i = 0; i < 10000; i++)
			validator.accept(bill);
	}

	/**
	 * With a bad bill, the sink should be ignored with the bill routed to the
	 * source; but if the source overloads, this should cause a SimulationException.
	 * 
	 * @throws OverloadException
	 *             If the device to which the bill is directed cannot hold this
	 *             bill.
	 * @throws DisabledException
	 *             If this device or the one to which the bill is directed is
	 *             disabled.
	 */
	@Test(expected = SimulationException.class)
	public void testAcceptBadBillWithFullSource() throws DisabledException, OverloadException {
		bill = new Bill(10, currency);
		FlowThroughEmitter<Bill> stub = new FlowThroughEmitter<Bill>() {
			@Override
			public boolean emit(Bill thing) throws DisabledException, OverloadException {
				throw new OverloadException();
			}
		};
		Acceptor<Bill> stub2 = new Acceptor<Bill>() {
			@Override
			public boolean hasSpace() {
				return true;
			}

			@Override
			public boolean accept(Bill thing) throws OverloadException, DisabledException {
				fail();
				return false;
			}
		};
		validator.connect(new BidirectionalChannel<>(stub, validator), new UnidirectionalChannel<>(stub2));
		validator.accept(bill);
	}
}
