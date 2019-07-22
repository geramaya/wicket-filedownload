package org.foo.myinc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.time.Duration;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private int counter = 4;
	private RepeatingView listItems;
	


	public HomePage(final PageParameters parameters) {
		// super(parameters);
		final Panel ajaxFileList = new AjaxFileListPanel("foo");
		ajaxFileList.setOutputMarkupId(true);
		ajaxFileList.add(new AjaxLink<Void>("addFile") {
			private static final long serialVersionUID = -2977890815053191418L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				listItems.add(createTempFile(Integer.toString(counter++)));
				target.add(ajaxFileList);
			}
		});
		
		listItems = new RepeatingView("listItems");
		listItems.setOutputMarkupId(true);

		listItems.add(createTempFile("1"));
		listItems.add(createTempFile("2"));
		listItems.add(createTempFile("3"));
		ajaxFileList.add(listItems);
		add(ajaxFileList);

	}

	private DownloadLink createTempFile(final String Id) {
		return new DownloadLink(Id, new AbstractReadOnlyModel<File>() {
			private static final long serialVersionUID = 1L;

			@Override
			public File getObject() {
				File tempFile;
				try {
					tempFile = File.createTempFile("wicket-" + Id + "-download-link--", ".tmp");
					String infoString = "Some data with download-link-Id: " + Id;
					InputStream data = new ByteArrayInputStream(infoString.getBytes());
					Files.writeTo(tempFile, data);

				} catch (IOException e) {
					throw new RuntimeException(e);
				}

				return tempFile;
			}
		}).setCacheDuration(Duration.NONE).setDeleteAfterDownload(true);
	}
}
