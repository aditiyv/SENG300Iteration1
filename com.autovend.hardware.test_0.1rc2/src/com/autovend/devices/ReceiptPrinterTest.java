package com.autovend.devices;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import com.autovend.devices.observers.AbstractDeviceObserver;
import com.autovend.devices.observers.ReceiptPrinterObserver;

@SuppressWarnings("javadoc")
public class ReceiptPrinterTest {
	private ReceiptPrinter printer;
	private int found; // used to record occurrences of events

	/**
	 * Default setup: printer is initialized with enough paper for two lines and
	 * enough ink for one completely full line of printing characters. This setup
	 * will be "undone" for certain test cases.
	 * 
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Before
	public void setup() throws OverloadException {
		printer = new ReceiptPrinter();
		printer.addPaper(2);
		printer.addInk(ReceiptPrinter.CHARACTERS_PER_LINE);
		found = 0;
	}

	/**
	 * With two lines of paper (and just enough ink for one full line), filling the
	 * first line and never advancing to the second, we should get an outOfInk event
	 * and nothing else.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testPrintFullLine() throws EmptyException, OverloadException {
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				found++;
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		for(int i = 0; i < ReceiptPrinter.CHARACTERS_PER_LINE; i++)
			printer.print('a');

		assertEquals(1, found);
	}

	/**
	 * With two lines of paper (and just enough ink for one full line), printing a
	 * single blankspace character, we should get an outOfInk event and nothing
	 * else.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testPrintBlankspace() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.addPaper(1);
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				found++;
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		// With one line of paper and no ink, the whitespace should take up a
		// character's worth of paper: an outOfInk event should happen and nothing else
		printer.print(' ');
		assertEquals(1, found);
	}

	/**
	 * With no lines of paper, printing a newline character should cause an
	 * EmptyException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = EmptyException.class)
	public void testNoPaper() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.addInk(1);
		printer.print('\n');
	}

	/**
	 * Printing more characters than fit on a line should cause an
	 * OverloadException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = OverloadException.class)
	public void testLineTooLong() throws OverloadException, EmptyException {
		// First, fill up the line
		for(int i = 0; i < ReceiptPrinter.CHARACTERS_PER_LINE; i++)
			printer.print(' ');

		// Now, register the observer in preparation of the real test; no events should
		// occur!
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		// Finally, print an extra character, which should be too much.
		printer.print(' ');
	}

	/**
	 * With one line of paper and just enough ink for one printing character,
	 * printing both a blank space and a printing character: we expect that an
	 * outOfInk event will occur and nothing else.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testTwoLines() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.addPaper(1);
		printer.addInk(1);

		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				found++;
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});
		printer.print(' ');
		printer.print('a');
		assertEquals(1, found);
	}

	/**
	 * Try printing a tab character with one line of paper and no ink. Nothing
	 * should happen.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testTab() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.addPaper(1);
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});
		printer.print('\t');
	}

	/**
	 * Try printing a newline character with one line of paper and no ink. An
	 * outOfPaper event should occur.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testPrintAndRunOutOfPaper() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.addPaper(1);
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				found++;
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				found++;
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});
		printer.print('\n');
		assertEquals(2, found);
	}

	/**
	 * Try printing a printing character with one line of paper and no ink. We
	 * expect an EmptyException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = EmptyException.class)
	public void testPrintWithoutInk() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.addPaper(1);
		printer.print('a');
	}

	/**
	 * If we try to remove a receipt that has not been cut, we should get null
	 * simulating that the user was unable to obtain the receipt. No events should
	 * be announced.
	 */
	@Test
	public void testRemoveWithoutCutting() {
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		assertEquals(null, printer.removeReceipt());
	}

	/**
	 * If we cut the paper without printing anything, the user should get an empty
	 * string representing a blank receipt. A second attempt without cutting should
	 * result in null because the cut receipt has already been removed. No events
	 * should be announced.
	 */
	@Test
	public void testCutAndRemove() {
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		printer.cutPaper();
		assertEquals("", printer.removeReceipt());
		assertEquals(null, printer.removeReceipt());
	}

	/**
	 * A negative quantity of ink should cause a SimulationException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = SimulationException.class)
	public void testAddNegativeInk() throws OverloadException, EmptyException {
		printer.addInk(-1);
	}

	/**
	 * A zero quantity of ink should cause nothing to happen.
	 */
	@Test
	public void testAddNoInk() throws OverloadException, EmptyException {
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		printer.addInk(0);
	}

	/**
	 * Adding more ink than the printer can hold should cause an OverloadException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = OverloadException.class)
	public void testAddTooMuchInk() throws OverloadException, EmptyException {
		printer.addInk(ReceiptPrinter.MAXIMUM_INK + 1);
	}

	/**
	 * A negative quantity of paper should cause a SimulationException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = SimulationException.class)
	public void testAddNegativePaper() throws OverloadException, EmptyException {
		printer.addPaper(-1);
	}

	/**
	 * Adding more paper than the printer can hold should cause an
	 * OverloadException.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test(expected = OverloadException.class)
	public void testAddTooMuchPaper() throws OverloadException, EmptyException {
		printer.addPaper(ReceiptPrinter.MAXIMUM_PAPER + 1);
	}

	/**
	 * A zero quantity of paper should cause nothing.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testAddNoPaper() throws OverloadException, EmptyException {
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		printer.addPaper(0);
	}

	/**
	 * Adding a quantity of ink that is equal to capacity should cause an inkAdded
	 * event and nothing else.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testAddMaximumInk() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				found++;
			}
		});

		printer.addInk(ReceiptPrinter.MAXIMUM_INK);
		assertEquals(1, found);
	}

	/**
	 * Adding a quantity of paper that is equal to capacity should cause a
	 * paperAdded event and nothing else.
	 * 
	 * @throws EmptyException
	 *             If we try to print when we lack either paper or ink.
	 * @throws OverloadException
	 *             If we exceed the paper or ink capacity.
	 */
	@Test
	public void testAddPaper() throws OverloadException, EmptyException {
		printer = new ReceiptPrinter();
		printer.register(new ReceiptPrinterObserver() {
			@Override
			public void reactToEnabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToDisabledEvent(AbstractDevice<? extends AbstractDeviceObserver> device) {
				fail();
			}

			@Override
			public void reactToPaperAddedEvent(ReceiptPrinter printer) {
				found++;
			}

			@Override
			public void reactToOutOfPaperEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToOutOfInkEvent(ReceiptPrinter printer) {
				fail();
			}

			@Override
			public void reactToInkAddedEvent(ReceiptPrinter printer) {
				fail();
			}
		});

		printer.addPaper(ReceiptPrinter.MAXIMUM_PAPER);
		assertEquals(1, found);
	}
}
