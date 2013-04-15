define([
	'jquery',
	'underscore',
	'backbone',
	'm/RowModel',
	'v/ColumnView'
], function($, _, Backbone, RowModel, ColumnView ) {

	var RowView = Backbone.View.extend({
		tagName: 'div',
		events: {
		},
		initialize: function() {
			var count = this.model.get('columnCount');
			var cls = 'row-' + count;
			this.$el.addClass(cls);

			var layout = this.model.get('layout');
			this.$el.addClass(layout);

			this.columnModels = this.model.getColumns();
			this.columnViews = [];
				
			this.columnModels.forEach(function(colModel) {
				var colView = new ColumnView({
					model: colModel
				});
				colView.parent = this;      
				this.columnViews.push(colView);
			}, this);

			this.model.bind('change:columnCount', this.columnCountUpdated, this);
		},
		render: function() {
			_.each(this.columnViews, function(colView) {
				var colEl = colView.render().el;
				this.$el.append(colEl);
			}, this);

			return this;
		},
		getTemplate: function() {
			var el = this.render().el;
			return $('<p></p>').append(el).html();
		},
		clearView: function() {
			_.each(this.columnViews, function(col) {
				col.remove();
			}, this);

			this.$el.empty();
		},
		columnCountUpdated: function() {
			var pre = this.model.previous('columnCount');
			var count = this.model.get('columnCount');

			this.$el.removeClass('row-' + pre)
				.addClass('row-' + count);
		},
		columnAdded: function() {
		},
		columnDeleted: function() {
		},
		columnUpdated: function() {
		},
		onRemoved: function() {
				
			this.unbind();
		}
	});

	return RowView;
});

