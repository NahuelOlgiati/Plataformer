package com.mandarina.lvlbuilder;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.w3c.dom.NodeList;

public class LvlBuilderMetada {

	public static void readFeatures(LvlBuilderImage img, PNGMetadata pngMetadata) {
		try (ImageInputStream input = ImageIO.createImageInputStream(img.getResource().getFile())) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = (ImageReader) readers.next(); // TODO: Validate that there are readers
			reader.setInput(input);
			IIOMetadata metadata = reader.getImageMetadata(0);
			for (TileFeature tf : TileFeature.values()) {
				for (RGB rgb : RGB.values()) {
					String key = PNGMetadata.getKey(rgb, tf.getKeyCode());
					String textEntry = getTextEntry(metadata, key);
					if (textEntry != null) {
						Map<Integer, Integer> coords = pngMetadata.getCoords(textEntry);
						pngMetadata.add(rgb, tf.getKeyCode(), coords);
					}
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void writeMetadata(File file, PNGMetadata pngMetadata) {
		try (ImageInputStream input = ImageIO.createImageInputStream(file);
				ImageOutputStream output = ImageIO.createImageOutputStream(file)) {
			Iterator<ImageReader> readers = ImageIO.getImageReaders(input);
			ImageReader reader = (ImageReader) readers.next(); // TODO: Validate that there are readers
			reader.setInput(input);
			IIOImage image = reader.readAll(0, null);
			IIOMetadata metadata = image.getMetadata();
			clearMetadata(metadata);
			for (Entry<String, Map<Integer, Integer>> e : pngMetadata.getMetadata().entrySet()) {
				String value = pngMetadata.toString(e.getKey());
				if (value != null) {
					addTextEntry(metadata, e.getKey(), value);
				}
			}
			ImageWriter writer = ImageIO.getImageWriter(reader); // TODO: Validate that there are writers
			writer.setOutput(output);
			writer.write(image);
		} catch (Throwable e) {
			System.out.println(e);
		}
	}

	private static void clearMetadata(final IIOMetadata metadata) throws IIOInvalidTreeException {
		metadata.setFromTree(IIOMetadataFormatImpl.standardMetadataFormatName,
				new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName));
	}

	private static void addTextEntry(final IIOMetadata metadata, final String key, final String value)
			throws IIOInvalidTreeException {
		IIOMetadataNode text = getTextEntry(key, value);
		IIOMetadataNode root = new IIOMetadataNode(IIOMetadataFormatImpl.standardMetadataFormatName);
		root.appendChild(text);
		metadata.mergeTree(IIOMetadataFormatImpl.standardMetadataFormatName, root);
	}

	private static IIOMetadataNode getTextEntry(final String key, final String value) {
		IIOMetadataNode textEntry = new IIOMetadataNode("TextEntry");
		textEntry.setAttribute("keyword", key);
		textEntry.setAttribute("value", value);
		IIOMetadataNode text = new IIOMetadataNode("Text");
		text.appendChild(textEntry);
		return text;
	}

	private static String getTextEntry(final IIOMetadata metadata, final String key) {
		IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName);
		NodeList entries = root.getElementsByTagName("TextEntry");
		for (int i = 0; i < entries.getLength(); i++) {
			IIOMetadataNode node = (IIOMetadataNode) entries.item(i);
			if (node.getAttribute("keyword").equals(key)) {
				return node.getAttribute("value");
			}
		}
		return null;
	}
}